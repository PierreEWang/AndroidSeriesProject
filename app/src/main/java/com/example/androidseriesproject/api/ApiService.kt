package com.example.androidseriesproject.api

import com.example.androidseriesproject.model.TvShowResponse
import com.example.androidseriesproject.model.TvShowDetails
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

    /**
     * Get detailed information about a specific TV show
     * CORRECTION: L'API renvoie un objet avec une propriété "tvShow", pas directement TvShowDetails
     *
     * @param showId The ID or permalink of the show to fetch details for
     * @return Response containing detailed TV show information wrapped in an object
     */
    @GET("show-details")
    suspend fun getShowDetails(@Query("q") showId: String): Response<TvShowDetailsResponse>

    /**
     * Search for TV shows by name
     *
     * @param query The search query
     * @param page The page number to fetch (default: 1)
     * @return Response containing search results
     */
    @GET("search")
    suspend fun searchShows(
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ): Response<TvShowResponse>
}

/**
 * Wrapper class for the show-details API response
 */
data class TvShowDetailsResponse(
    val tvShow: TvShowDetails
)