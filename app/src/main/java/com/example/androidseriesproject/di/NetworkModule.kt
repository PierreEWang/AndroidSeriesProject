package com.example.androidseriesproject.di

import com.example.androidseriesproject.data.remote.ShowApiService
import com.example.androidseriesproject.data.repository.ShowRepository
import com.example.androidseriesproject.data.repository.ShowRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl() = "https://www.episodate.com/api/"

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ShowApiService {
        return retrofit.create(ShowApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.episodate.com/api/") // EpisoDate API base URL
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
    fun provideTvShowRepository(apiService: com.example.androidseriesproject.api.ApiService): com.example.androidseriesproject.repository.TvShowRepository {
        return com.example.androidseriesproject.repository.TvShowRepositoryImpl(apiService)
    }
}

