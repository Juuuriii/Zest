package com.example.zest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zest.data.Repository
import com.example.zest.data.model.Day
import com.example.zest.data.model.Entry
import com.example.zest.data.model.ZestUser
import com.example.zest.data.remote.QuoteApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class FirebaseViewModel : ViewModel() {

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

    fun createEntry(title: String, text: String, date: String = curDate.value.toString()) {
        if (title.isNotEmpty() && text.isNotEmpty()) {
            usersRef.document(firebaseAuth.currentUser!!.uid)
                .collection("journal")
                .document(date)
                .collection("entries")
                .document(Entry().time)
                .set(Entry(title = title, text = text))
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

    //DATE

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


}