package com.example.zest.data.model

import com.example.zest.utils.TimeHandler
import com.google.firebase.firestore.DocumentId
import java.time.LocalDate

data class Entry(

    val title: String = "",

    val text: String = "",

    var tags: MutableList<String> = mutableListOf(),

    var time: String = TimeHandler().getDateTimeHoursMinsSecs(),

    var date: String = LocalDate.now().toString()

)
