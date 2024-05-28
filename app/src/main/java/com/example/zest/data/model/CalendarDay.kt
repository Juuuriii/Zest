package com.example.zest.data.model

import java.time.LocalDate

data class CalendarDay(

    val day: String,
    val month: String,
    val year: String,
    var hasEntry: Boolean,
    var isToday: Boolean

)
