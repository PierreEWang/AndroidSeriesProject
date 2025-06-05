package com.example.androidseriesproject.repository

import com.example.androidseriesproject.model.TvShow
import com.example.androidseriesproject.model.TvShowResponse
import com.example.androidseriesproject.model.TvShowDetails
import com.example.androidseriesproject.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for TV show data operations
 */
interface TvShowRepository {
    /**
     * Get the most popular TV shows with pagination
     *
     * @param page The page number to fetch
     * @return Flow of Resource wrapping the TV show response
     */
    fun getMostPopularShows(page: Int = 1): Flow<Resource<TvShowResponse>>

    /**
     * Get detailed information about a specific TV show
     *
     * @param showId The ID of the TV show to fetch details for
     * @return Flow of Resource wrapping the TV show details
     */
    fun getShowDetails(showId: Int): Flow<Resource<TvShowDetails>>

    /**
     * Search for TV shows by name
     *
     * @param query The search query
     * @param page The page number to fetch
     * @return Flow of Resource wrapping the search results
     */
    fun searchShows(query: String, page: Int = 1): Flow<Resource<TvShowResponse>>
}