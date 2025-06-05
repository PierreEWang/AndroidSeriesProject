package com.example.androidseriesproject.repository

import com.example.androidseriesproject.model.TvShow
import com.example.androidseriesproject.model.TvShowResponse
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
     * Get a specific TV show by ID
     *
     * @param id The ID of the TV show to fetch
     * @return Flow of Resource wrapping the TV show
     */
    fun getTvShowById(id: Int): Flow<Resource<TvShow>>
}