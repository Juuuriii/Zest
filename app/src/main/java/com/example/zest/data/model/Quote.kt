package com.example.zest.data.model

import com.squareup.moshi.Json

data class Quote(

    @Json(name = "q")
    val text: String,

    @Json(name = "a")
    val author: String

)
