package com.example.androidseriesproject.test

import android.util.Log
import com.example.androidseriesproject.api.RetrofitClient
import com.example.androidseriesproject.model.TvShowResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

/**
 * Test class for the EpisoDate API
 */
object ApiTest {
    private const val TAG = "ApiTest"

    /**
     * Tests the API call to fetch the most popular TV shows
     */
    fun testMostPopularShows() {
        Log.d(TAG, "Starting API test for most popular shows")
        
        // Launch a coroutine in the IO dispatcher for network operations
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Make the API call
                val response = RetrofitClient.apiService.getMostPopularShows(page = 1)
                
                // Switch to Main dispatcher to update UI or log results
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Handle successful response
                        val tvShowResponse = response.body()
                        handleSuccessResponse(tvShowResponse)
                    } else {
                        // Handle error response
                        Log.e(TAG, "API call failed with error code: ${response.code()}")
                        Log.e(TAG, "Error message: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: UnknownHostException) {
                // Handle network connectivity issues
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "Network connectivity issue: Unable to reach the server", e)
                    Log.e(TAG, "Please check your internet connection and try again")
                    Log.e(TAG, "Error details: ${e.message}")
                }
            } catch (e: Exception) {
                // Handle other exceptions
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "Exception during API call: ${e.javaClass.simpleName}", e)
                    Log.e(TAG, "Error details: ${e.message}")
                }
            }
        }
    }

    /**
     * Handles successful API response
     */
    private fun handleSuccessResponse(tvShowResponse: TvShowResponse?) {
        tvShowResponse?.let { response ->
            Log.d(TAG, "API call successful!")
            Log.d(TAG, "Total shows: ${response.total}")
            Log.d(TAG, "Current page: ${response.page} of ${response.pages}")
            Log.d(TAG, "Shows in this response: ${response.tvShows.size}")
            
            // Log details of each TV show
            response.tvShows.forEachIndexed { index, show ->
                Log.d(TAG, "Show ${index + 1}: ${show.name}")
                Log.d(TAG, "  ID: ${show.id}")
                Log.d(TAG, "  Network: ${show.network}")
                Log.d(TAG, "  Status: ${show.status}")
                Log.d(TAG, "  Start date: ${show.startDate}")
                Log.d(TAG, "  Country: ${show.country}")
            }
        } ?: run {
            Log.e(TAG, "Response body is null")
        }
    }
}