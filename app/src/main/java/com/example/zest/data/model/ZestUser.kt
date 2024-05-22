package com.example.zest.data.model

data class ZestUser(

    val username: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val usedTags: MutableList<String> = mutableListOf()

)
