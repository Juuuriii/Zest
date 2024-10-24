package com.example.zest.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class AuthClient {

    private val auth = Firebase.auth

    fun register(email: String, password: String, completion: (FirebaseUser?) -> Unit) {
        if (email.isNotBlank() && password.isNotBlank()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        auth.currentUser?.sendEmailVerification()
                        logout()
                        completion(auth.currentUser)
                    } else {
                        Log.e("FIREBASE_AUTH", result.exception.toString())
                    }
                }
        }
    }



    fun logout() {
        auth.signOut()
    }
}