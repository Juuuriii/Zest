package com.example.zest.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.zest.data.model.Quote
import com.example.zest.data.remote.QuoteApi

class Repository(
    private val apiSirvice: QuoteApi
) {
    private var _quoteList = MutableLiveData<List<Quote>>()

    val quoteList: LiveData<List<Quote>>
        get() = _quoteList

    suspend fun getQuotes() {
        try {
            val result = apiSirvice.retrofitService.getQuotes()
            _quoteList.postValue(result)
        } catch (e: Exception) {
            Log.e("QuoteApi", "${e.stackTrace}")
        }
    }
}