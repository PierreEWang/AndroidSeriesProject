package com.example.androidseriesproject.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton object to initialize and provide the Retrofit instance
 */
object RetrofitClient {
    private const val BASE_URL = "https://www.episodate.com/api/"
    private const val TIMEOUT = 30L // 30 seconds timeout
    
    /**
     * Creates and returns an instance of the ApiService interface
     */
    val apiService: ApiService by lazy {
        createRetrofit().create(ApiService::class.java)
    }
    
    /**
     * Creates and configures the Retrofit instance
     */
    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
    
    /**
     * Creates and configures the OkHttpClient with logging and timeouts
     */
    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
}