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

    /***
     * Retrofit 2.6.0 or higher supports coroutines
     * This suspend funciton is main-safe so you can call
     * easily from Dispatchers.Main
     */
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