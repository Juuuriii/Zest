package com.example.zest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zest.data.Repository
import com.example.zest.data.model.Entry
import com.example.zest.data.model.ZestUser
import com.example.zest.data.remote.QuoteApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class FirebaseViewModel : ViewModel() {

    private val firebaseAuth = Firebase.auth

    private val firestoreDatabase = Firebase.firestore

    private val repository = Repository(QuoteApi)

    val quotes = repository.quoteList

    private var _curUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)

    val curUser: LiveData<FirebaseUser?>
        get() = _curUser

    var _username = MutableLiveData<String>()

    val username: LiveData<String>
        get() = _username

    private lateinit var userRef: DocumentReference

    init {
        loadQuotes()
        getUsername()
    }

    fun loginWithEmailAndPassword(email: String, pwd: String) {
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

    fun registerWithEmailAndPassword(
        email: String,
        pwd: String,
        username: String,
        completion: () -> Unit
    ) {
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

    fun createUser(username: String) {

        val users = firestoreDatabase.collection("users")

        users
            .document(firebaseAuth.currentUser!!.uid)
            .set(ZestUser(username))
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "Firestore",
                    "DocumentSnapshot added with ID: ${firebaseAuth.currentUser!!.uid}"
                )
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }


        users
            .document(firebaseAuth.currentUser!!.uid)
            .collection("journal")
            .document(LocalDate.now().toString())
            .set(Entry())

    }

    fun logout() {
        firebaseAuth.signOut()
        _curUser.value = firebaseAuth.currentUser
    }


    private fun loadQuotes() {

        viewModelScope.launch(Dispatchers.IO) {
            repository.getQuotes()
        }

    }

    fun getUsername() {

        val docRef = firestoreDatabase.collection("users").document(firebaseAuth.currentUser!!.uid)

        val source = Source.CACHE


        docRef.get(source).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // Document found in the offline cache
                val document = task.result

                _username.value = document.data?.get("username").toString()

                Log.d("ΩFirestoreUsername", "Cached document data: ${username}")


            } else {
                Log.d("ΩFirestoreUsername", "Cached get failed: ", task.exception)
            }



        }


    }

}