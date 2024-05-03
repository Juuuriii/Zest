package com.example.zest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zest.data.Repository
import com.example.zest.data.remote.QuoteApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseViewModel: ViewModel() {

    val firebaseAuth = Firebase.auth

    private val repository = Repository(QuoteApi)

    val quotes = repository.quoteList

    private var _curUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)

    val curUser: LiveData<FirebaseUser?>
        get() = _curUser

    init {
        loadQuotes()
    }

    fun loginWithEmailAndPassword(email: String, pwd: String) {
        if (email.isNotBlank() && pwd.isNotBlank()) {
            firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener { authResult ->
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
            firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {

                    logout()

                } else {
                    Log.e("FIREBASE_AUTH", authResult.exception.toString())
                }
                completion()
            }
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        _curUser.value = firebaseAuth.currentUser
    }

    private fun loadQuotes(){

        viewModelScope.launch(Dispatchers.IO) {
            repository.getQuotes()
        }

    }

}