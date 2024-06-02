package com.example.zest.data.model

import com.example.zest.utils.TimeHandler
import java.time.LocalDate

data class Entry(

    val title: String = "",

    val text: String = "",

    var tags: MutableList<String> = mutableListOf(),

    var time: String = TimeHandler().getDateTimeHoursMinsSecs(),

    var date: String = LocalDate.now().toString(),

    val day: String = LocalDate.now().dayOfMonth.toString(),

    val month: String = LocalDate.now().month.toString(),

    val year: String = LocalDate.now().year.toString(),

    val userId: String = "",

    val keyWordsText: List<String> = listOf(),

    val keyWordTitle: String = "",

    val keyWordsTags: List<String> = listOf()

)
