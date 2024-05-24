package com.example.zest

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.zest.data.Repository
import com.example.zest.data.adapter.JournalEntryAdapter
import com.example.zest.data.adapter.TagEditAdapter
import com.example.zest.data.model.Entry
import com.example.zest.data.model.JournalDay
import com.example.zest.data.model.ZestUser
import com.example.zest.data.remote.QuoteApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.checkerframework.common.value.qual.EnsuresMinLenIf
import java.time.LocalDate

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {

    private val tagList = mutableListOf(
        "Work",
        "Food",
        "Tennis",
        "Games"
    ) //TODO(Safe used tags for AutocompleteTextView)

    private val firebaseAuth = Firebase.auth

    private val firestoreDatabase = Firebase.firestore

    private val usersRef = firestoreDatabase.collection("users")

    private val repository = Repository(QuoteApi)

    val quotes = repository.quoteList

    private var _curUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val curUser: LiveData<FirebaseUser?>
        get() = _curUser

    private var _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username

    private var _curDate = MutableLiveData<LocalDate>(LocalDate.now())

    val curDate: LiveData<LocalDate>
        get() = _curDate

    private var _curEntry = MutableLiveData<Entry>()

    val curEntry: LiveData<Entry>
        get() = _curEntry

    private var _curEntryTags = MutableLiveData<MutableList<String>>()

    val curEntryTags: LiveData<MutableList<String>>
        get() = _curEntryTags

   private var _curEntryPosition = MutableLiveData<Int>()

    val curEntryPosition: LiveData<Int>
        get() = _curEntryPosition

    private var _journalDay = MutableLiveData<JournalDay>()

    val journalDay: LiveData<JournalDay>
        get() = _journalDay

    init {
        loadQuotes()
    }

    private fun loadQuotes() {

        viewModelScope.launch(Dispatchers.IO) {

            repository.getQuotes()

        }
    }

    fun register(email: String, pwd: String, username: String, completion: () -> Unit) {
        if (email.isNotBlank() && pwd.isNotBlank()) {
            firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {

                        createUser(username)
                        completion()

                    } else {
                        Log.e("FIREBASE_AUTH", authResult.exception.toString())
                    }
                }
        }
    }

    fun login(email: String, pwd: String) {
        if (email.isNotBlank() && pwd.isNotBlank()) {
            firebaseAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        _curUser.value = firebaseAuth.currentUser
                    } else {
                        Log.e("FIREBASE_AUTH", authResult.exception.toString())
                    }
                }
        }
    }

    private fun createUser(username: String) {

        usersRef
            .document(firebaseAuth.currentUser!!.uid)
            .set(
                ZestUser(
                    username = username,
                    userId = firebaseAuth.currentUser!!.uid,
                    userEmail = firebaseAuth.currentUser?.email ?: ""
                )
            )
            .addOnSuccessListener {
                Log.d(
                    "Firestore", "DocumentSnapshot added with ID: ${firebaseAuth.currentUser!!.uid}"
                )
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }
    }

    fun createEntry(entry: Entry) {

        if (entry.title.isNotEmpty() && entry.text.isNotEmpty()) {



            val newEntryList = _journalDay.value?.entries ?: mutableListOf()

            newEntryList.add(entry)

            newEntryList.sortBy { it.time }

            usersRef.document(firebaseAuth.currentUser!!.uid)
                .collection("journal")
                .document(entry.date)
                .set(JournalDay(entry.date, newEntryList))

        }


    }

    fun getJournalDay(date: String){

        usersRef.document(firebaseAuth.currentUser!!.uid)
            .collection("journal")
            .document(date)
            .get().addOnSuccessListener { snapshot ->

                _journalDay.value = snapshot.toObject(JournalDay::class.java)

                if(_journalDay.value == null){

                    usersRef.document(firebaseAuth.currentUser!!.uid)
                        .collection("journal")
                        .document(date)
                        .set(JournalDay(date))

                    getJournalDay(date)

                }

                Log.d(
                    "ΩEntry", "${_journalDay.value?.date} : ${_journalDay.value?.entries}"
                )

            }.addOnFailureListener {
                Log.d(
                    "ΩEntry", "Failure: ${_journalDay.value?.entries}"
                )
            }
    }

    fun logout() {
        firebaseAuth.signOut()
        _curUser.value = firebaseAuth.currentUser
    }

    fun resetDateToCurrentDate() {

        _curDate.value = LocalDate.now()

    }

    fun curDatePlusOne() {

        if (_curDate.value != LocalDate.now()) {

            _curDate.value = _curDate.value!!.plusDays(1)


        }
    }

    fun curDateMinusOne() {

        _curDate.value = _curDate.value!!.minusDays(1)

    }


    val deleteEntry: (position: Int) -> Unit = { position ->

        _journalDay.value!!.entries.removeAt(position)

        usersRef.document(firebaseAuth.currentUser!!.uid)
            .collection("journal")
            .document(curDate.value.toString())
            .set(_journalDay.value!!)

        _curDate.value = _curDate.value

    }

    val setCurEntry: (entry: Entry, position: Int) -> Unit = { entry, position ->

        _curEntry.value = entry
        _curEntryTags.value = entry.tags
        _curEntryPosition.value = position

    }

    fun updateEntry(entry: Entry, position: Int) {

        _journalDay.value!!.entries[position] = entry

        usersRef.document(firebaseAuth.currentUser!!.uid)
            .collection("journal")
            .document(curDate.value.toString())
            .set(_journalDay.value!!)


    }

    fun setEmptyEntry() {
        _curEntry.value = Entry()
        _curEntryTags.value = mutableListOf()
    }

    val deleteTag: (position: Int) -> Unit = { position ->
        _curEntry.value!!.tags.removeAt(position)
        _curEntryTags.value = _curEntryTags.value
        Log.i("ΩTags", "${_curEntry.value!!.tags}")
    }

    val addTag: (context: Context, adapter: TagEditAdapter) -> Unit = { context, adapter ->

        val addTagAlertDialogView = LayoutInflater.from(context).inflate(R.layout.add_tag_dialog, null)

        val addTagAlertDialogBuilder = AlertDialog.Builder(context).setView(addTagAlertDialogView)

        val addTagAlertDialog = addTagAlertDialogBuilder.show()


        addTagAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val arrayAdapter =
            ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, tagList)
        addTagAlertDialog.findViewById<AutoCompleteTextView>(R.id.etTag).setAdapter(arrayAdapter)

        addTagAlertDialogView.findViewById<Button>(R.id.btnAdd).setOnClickListener {

            addTagAlertDialog.dismiss()

            val tag = addTagAlertDialogView.findViewById<EditText>(R.id.etTag).text.toString()

            if (tag.isNotEmpty() && !_curEntry.value!!.tags.contains(tag)) {

                _curEntryTags.value!!.add(0,tag)
                _curEntryTags.value = _curEntryTags.value

                Log.i("ΩTags", "${_curEntry.value!!.tags}")

                addTagAlertDialog.dismiss()
            } else {

                addTagAlertDialog.dismiss()

            }
        }

        addTagAlertDialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {

            addTagAlertDialog.dismiss()

        }

    }

    fun getUsername() {

        val docRef = firestoreDatabase.collection("users").document(firebaseAuth.currentUser!!.uid)

        val source = Source.CACHE

        docRef.get(source).addOnCompleteListener() { task ->
            if (task.isSuccessful) {

                val document = task.result

                _username.value = document.data?.get("username").toString()

            } else {

                Log.d("ΩFirestoreUsername", "Cached get failed: ", task.exception)

            }
        }
    }

}





