package com.example.androidseriesproject.data.repository

import com.example.androidseriesproject.data.remote.ShowApiService
import com.example.androidseriesproject.data.model.ShowResponse
import javax.inject.Inject

class ShowRepositoryImpl @Inject constructor(
	private val apiService: ShowApiService
) : ShowRepository {
	override suspend fun getPopularShows(page: Int): ShowResponse {
		return apiService.getPopularShows(page)
	}
}
