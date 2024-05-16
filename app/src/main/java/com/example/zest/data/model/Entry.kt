package com.example.zest.data.model

import com.example.zest.data.TimeHandler
import com.google.firebase.firestore.DocumentId
import com.google.type.DateTime
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

data class Entry(

    val title: String = "Title",

    val text: String = "This is a journal entry",

    val tags: List<String> = listOf("Work", "Hobby"),

    val time: String = TimeHandler().getDateTime()

)
