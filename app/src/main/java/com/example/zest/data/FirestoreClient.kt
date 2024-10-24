package com.example.zest.data

import android.util.Log
import com.example.zest.data.model.ZestUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore

class FirestoreClient {

    private val store = Firebase.firestore

    fun createZestUser(username: String, userAuth: FirebaseUser?) {

        val user = userAuth ?: return

        store.collection("users")
            .document(user.uid)
            .set(
                ZestUser(
                    username = username,
                    userId = user.uid,
                    userEmail = user.email ?: return,
                    profilePicPath = "Users/default/profilePic/stats.png"
                )
            )
            .addOnFailureListener { exception ->
                Log.w("createZestUser", "$exception")
            }
    }
}