package com.example.zest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.zest.data.AuthClient
import com.example.zest.data.FirestoreClient
import com.google.firebase.auth.FirebaseUser

class AuthViewModel {

    private val authClient = AuthClient()
    private val firestoreClient = FirestoreClient()

    private var _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    fun register(email: String, password: String, username: String, completion: () -> Unit) {
        authClient.register(email, password) { user ->
            firestoreClient.createZestUser(username, user)
        }
        completion()
    }
}