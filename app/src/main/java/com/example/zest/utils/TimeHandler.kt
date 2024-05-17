package com.example.zest.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeHandler() {
    fun getDateTime():String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date = Date()
        return inputFormat.format(date.time)
    }

    fun formatLocalDate(date: String): String {

        val list = date.split("-")

        return list[2] + "." + list[1] + "." + list[0]
    }

}