package com.example.androidseriesproject.data.remote

import com.example.androidseriesproject.data.model.ShowResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowApiService {
	@GET("most-popular")
	suspend fun getPopularShows(@Query("page") page: Int = 1): ShowResponse
}
