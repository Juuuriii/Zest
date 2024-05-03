package com.example.zest.data.remote

import com.example.zest.data.model.Quote
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://zenquotes.io/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
private val client = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface QuoteApiService {
    @GET("quotes")
    suspend fun getQuotes(): List<Quote>

}

object QuoteApi {

    val retrofitService: QuoteApiService by lazy { retrofit.create(QuoteApiService::class.java)}

}