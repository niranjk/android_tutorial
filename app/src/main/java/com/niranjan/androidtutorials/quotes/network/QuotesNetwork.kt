package com.niranjan.androidtutorials.quotes.network

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Network interface to fetch a new quotes
 */
interface QuotesNetwork {
    @GET("quotes.json")
    fun fetchQuotesCallback(): Call<String>

    @GET("quotes.json")
    suspend fun fetchQuotes(): String
}

val quotesNetworkService : QuotesNetwork by lazy {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(SkipNetworkInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(QuotesNetwork::class.java)
}

fun getService() = quotesNetworkService