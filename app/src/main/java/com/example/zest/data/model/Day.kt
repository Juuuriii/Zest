package com.example.zest.data.model


import com.google.firebase.firestore.DocumentId
import java.time.LocalDate

data class Day(


    val date: String = LocalDate.now().toString()


)
