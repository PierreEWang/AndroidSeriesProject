package com.example.androidseriesproject.repository

import com.example.androidseriesproject.api.ApiService
import com.example.androidseriesproject.model.TvShow
import com.example.androidseriesproject.model.TvShowResponse
import com.example.androidseriesproject.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of the TvShowRepository interface
 *
 * @param apiService The API service used to make network requests
 */
class TvShowRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TvShowRepository {

    /**
     * Get the most popular TV shows with pagination
     *
     * @param page The page number to fetch
     * @return Flow of Resource wrapping the TV show response
     */
    override fun getMostPopularShows(page: Int): Flow<Resource<TvShowResponse>> = flow {
        emit(Resource.loading())

        try {
            val response = apiService.getMostPopularShows(page)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.success(it))
                } ?: emit(Resource.error("Response body is null"))
            } else {
                emit(Resource.error("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: IOException) {
            emit(Resource.error("Network error: ${e.localizedMessage}", e))
        } catch (e: HttpException) {
            emit(Resource.error("HTTP error: ${e.localizedMessage}", e))
        } catch (e: Exception) {
            emit(Resource.error("Unknown error: ${e.localizedMessage}", e))
        }
    }

    /**
     * Get a specific TV show by ID
     *
     * Note: This is a placeholder as the current API doesn't seem to have an endpoint
     * for fetching a single TV show by ID. In a real implementation, you would call
     * the appropriate API endpoint.
     *
     * @param id The ID of the TV show to fetch
     * @return Flow of Resource wrapping the TV show
     */
    override fun getTvShowById(id: Int): Flow<Resource<TvShow>> = flow {
        emit(Resource.loading())

        // This is a placeholder. In a real implementation, you would call the API
        // For now, we'll just emit an error
        emit(Resource.error("Fetching TV show by ID is not implemented in the current API"))
    }
}