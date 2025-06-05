package com.example.androidseriesproject.repository

import com.example.androidseriesproject.api.ApiService
import com.example.androidseriesproject.model.TvShow
import com.example.androidseriesproject.model.TvShowResponse
import com.example.androidseriesproject.model.TvShowDetails
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
     * Get detailed information about a specific TV show
     * CORRECTION: Gestion du wrapper de réponse API
     *
     * @param showId The ID of the TV show to fetch details for
     * @return Flow of Resource wrapping the TV show details
     */
    override fun getShowDetails(showId: Int): Flow<Resource<TvShowDetails>> = flow {
        emit(Resource.loading())

        try {
            val response = apiService.getShowDetails(showId.toString())
            if (response.isSuccessful) {
                response.body()?.let { detailsResponse ->
                    // Extraire l'objet TvShowDetails du wrapper
                    emit(Resource.success(detailsResponse.tvShow))
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
     * Search for TV shows by name
     *
     * @param query The search query
     * @param page The page number to fetch
     * @return Flow of Resource wrapping the search results
     */
    override fun searchShows(query: String, page: Int): Flow<Resource<TvShowResponse>> = flow {
        emit(Resource.loading())

        try {
            val response = apiService.searchShows(query, page)
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
}