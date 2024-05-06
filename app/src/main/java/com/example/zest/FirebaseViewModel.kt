package com.example.zest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zest.data.Repository
import com.example.zest.data.model.Journal
import com.example.zest.data.model.ZestUser
import com.example.zest.data.remote.QuoteApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
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

    private lateinit var userRef: DocumentReference

    init {
        loadQuotes()
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

    fun registerWithEmailAndPassword(email: String, pwd: String, completion: () -> Unit) {
        if (email.isNotBlank() && pwd.isNotBlank()) {
            firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {

                        createUser()
                        completion()


                    } else {
                        Log.e("FIREBASE_AUTH", authResult.exception.toString())
                    }

                }
        }
    }

    fun createUser() {

        val users = firestoreDatabase.collection("users")

        users
            .document(firebaseAuth.currentUser!!.uid)
            .set(ZestUser())
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "DocumentSnapshot added with ID: ${firebaseAuth.currentUser!!.uid}")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }
        users
            .document(firebaseAuth.currentUser!!.uid)
            .collection("journal")
            .document(LocalDate.now().toString())
            .set(Journal())


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

}