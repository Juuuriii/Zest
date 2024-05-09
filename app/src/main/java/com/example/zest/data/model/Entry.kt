package com.example.zest.data.model

data class Entry(

    val title: String = "Title",

    val text: String = "This is a journal entry",

    val tags: List<String> = listOf("Work", "Hobby")

)
