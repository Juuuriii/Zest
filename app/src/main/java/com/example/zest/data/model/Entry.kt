package com.example.zest.data.model

import com.example.zest.utils.TimeHandler
import java.time.LocalDate

data class Entry(

    val title: String = "",

    val text: String = "",

    val tags: List<String> = listOf(),

    val time: String = TimeHandler().getDateTime(),

    val date: String = LocalDate.now().toString()

)
