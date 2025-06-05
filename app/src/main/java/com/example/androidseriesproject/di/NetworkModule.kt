package com.example.androidseriesproject.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.google.gson.Gson
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl() = "https://www.episodate.com/api/"

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.episodate.com/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): com.example.androidseriesproject.api.ApiService {
        return retrofit.create(com.example.androidseriesproject.api.ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTvShowRepository(
        apiService: com.example.androidseriesproject.api.ApiService
    ): com.example.androidseriesproject.repository.TvShowRepository {
        return com.example.androidseriesproject.repository.TvShowRepositoryImpl(apiService)
    }
}

