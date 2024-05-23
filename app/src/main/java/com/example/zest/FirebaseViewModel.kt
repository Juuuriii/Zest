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
import com.example.zest.data.model.Entry
import com.example.zest.data.model.JournalDay
import com.example.zest.data.model.ZestUser
import com.example.zest.data.remote.QuoteApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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

    private var _entryListOfDay = MutableLiveData<List<Entry>>()

    val entryListOfDay: LiveData<List<Entry>>
        get() = _entryListOfDay

    private var _journalDays = MutableLiveData<List<JournalDay>>()
    val journalDays: LiveData<List<JournalDay>>
        get() = _journalDays

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

    fun createEntry(
        title: String,
        text: String,
        tags: MutableList<String>,
        date: String = curDate.value.toString()
    ) {
        if (title.isNotEmpty() && text.isNotEmpty()) {

            usersRef.document(firebaseAuth.currentUser!!.uid)
                .collection("journal")
                .document(date)
                .set(JournalDay(date))


            usersRef.document(firebaseAuth.currentUser!!.uid)
                .collection("journal")
                .document(date)
                .collection("entries")
                .document(Entry().time)
                .set(Entry(title = title, text = text, tags = tags))
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

    fun getEntryRef(date: String): CollectionReference {

        return usersRef.document(firebaseAuth.currentUser!!.uid)
            .collection("journal")
            .document(date)
            .collection("entries")

    }

    val deleteEntry: (time: String) -> Unit = { time ->

        _curDate.value = _curDate.value
        getEntryRef(curDate.value.toString()).document(time).delete()

    }

    val setCurEntry: (entry: Entry) -> Unit = { entry ->

        _curEntry.value = entry

    }

    fun updateEntry(entry: Entry) {

        getEntryRef(curDate.value.toString()).document(entry.time)
            .update(
                mapOf(
                    "title" to entry.title,
                    "text" to entry.text,
                    "tags" to entry.tags
                )
            )

    }

    fun setEmptyEntry() {
        _curEntry.value = Entry()
    }

    val deleteTag: (position: Int) -> Unit = { position ->
        _curEntry.value!!.tags.removeAt(position)
        _curEntry.value = _curEntry.value
        Log.i("ΩTags", "${_curEntry.value!!.tags}")
    }

    val addTag: (context: Context) -> Unit = {

        val addTagAlertDialogView = LayoutInflater.from(it).inflate(R.layout.add_tag_dialog, null)

        val addTagAlertDialogBuilder = AlertDialog.Builder(it).setView(addTagAlertDialogView)

        val addTagAlertDialog = addTagAlertDialogBuilder.show()


        addTagAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val arrayAdapter =
            ArrayAdapter<String>(it, android.R.layout.simple_dropdown_item_1line, tagList)
        addTagAlertDialog.findViewById<AutoCompleteTextView>(R.id.etTag).setAdapter(arrayAdapter)

        addTagAlertDialogView.findViewById<Button>(R.id.btnAdd).setOnClickListener {

            addTagAlertDialog.dismiss()

            val tag = addTagAlertDialogView.findViewById<EditText>(R.id.etTag).text.toString()

            if (tag.isNotEmpty() && !_curEntry.value!!.tags.contains(tag)) {

                _curEntry.value!!.tags.add(0, tag)
                _curEntry.value = _curEntry.value
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

    fun getAllJournalDays() {

        firestoreDatabase
            .collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("journal")
            .get().addOnSuccessListener { querySnapshot ->

                _journalDays.value = querySnapshot.map { it.toObject(JournalDay::class.java) }

                Log.i("ΩJournalDays", "${_journalDays.value}")
            }

    }

    fun getEntryOfDay(date: String){

        getEntryRef(date).get().addOnSuccessListener { querySnapshot ->

            _entryListOfDay.value = querySnapshot.map { it.toObject(Entry::class.java) }

        }


    }



}





