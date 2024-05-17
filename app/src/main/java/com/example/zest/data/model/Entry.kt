package com.example.zest.data.model

import com.example.zest.utils.TimeHandler

data class Entry(

    val title: String = "Title",

    val text: String = "This is a journal entry",

    val tags: List<String> = listOf("Work", "Hobby"),

    val time: String = TimeHandler().getDateTime()

)
