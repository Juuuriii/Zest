package com.example.zest.utils

import java.text.SimpleDateFormat
import java.time.YearMonth
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

    fun formatDateDayMonthNameYear(date: String): String {

        val list = date.split("-")

        return when(list[1]){

            "01" -> {list[2] + "  January  " + list[0]}

            "02" -> {list[2] + "  February  " + list[0]}

            "03" -> {list[2] + "  March  " + list[0]}

            "04" -> {list[2] + "  April  " + list[0]}

            "05" -> {list[2] + "  May  " + list[0]}

            "06" -> {list[2] + "  June  " + list[0]}

            "07" -> {list[2] + "  July  " + list[0]}

            "08" -> {list[2] + "  August  " + list[0]}

            "09" -> {list[2] + "  September  " + list[0]}

            "10" -> {list[2] + "  October  " + list[0]}

            "11" -> {list[2] + "  November  " + list[0]}

            "12" -> {list[2] + "  December  " + list[0]}

            else -> {"Invalid Date"}
        }

    }

    fun formatYearMonth(yearMonth: YearMonth): String{

        val list = yearMonth.toString().split("-")

       return when(list[1]) {

           "01" -> {"January " + list[0]}

           "02" -> {"February " + list[0]}

           "03" -> {"March " + list[0]}

           "04" -> {"April  " + list[0]}

           "05" -> {"May  " + list[0]}

           "06" -> {"June  " + list[0]}

           "07" -> {"July  " + list[0]}

           "08" -> {"August  " + list[0]}

           "09" -> {"September  " + list[0]}

           "10" -> {"October  " + list[0]}

           "11" -> {"November  " + list[0]}

           "12" -> {"December  " + list[0]}

           else -> {"  "}
       }


    }

}