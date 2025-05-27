package com.example.androidseriesproject.api

import com.example.androidseriesproject.model.TvShowResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

/**
 * Retrofit interface for the EpisoDate API
 * Base URL: https://www.episodate.com/api/
 */
interface ApiService {
    /**
     * Get the most popular TV shows with pagination support
     *
     * @param page The page number to fetch (default: 1)
     * @return Response containing the most popular TV shows
     */
    @GET("most-popular")
    suspend fun getMostPopularShows(@Query("page") page: Int = 1): Response<TvShowResponse>
}