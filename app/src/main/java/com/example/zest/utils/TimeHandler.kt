package com.example.zest.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeHandler() {
    fun getDateTimeHoursMinsSecs():String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date = Date()
        return inputFormat.format(date.time)
    }

    fun formatDateTimeHoursMins(time: String): String {

        val list = time.split(":")

        return list[0] + ":" + list[1]

    }

    fun formatLocalDate(date: String): String {

        val list = date.split("-")

        return list[2] + "." + list[1] + "." + list[0]
    }

}