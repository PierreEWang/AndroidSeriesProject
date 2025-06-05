package com.example.androidseriesproject.data.repository

import com.example.androidseriesproject.data.remote.ShowApiService
import com.example.androidseriesproject.model.TvShowResponse
import javax.inject.Inject

class ShowRepositoryImpl @Inject constructor(
	private val apiService: ShowApiService
) : TvShowRepository {
	override suspend fun getPopularShows(page: Int): TvShowResponse {
		return apiService.getPopularShows(page)
	}
}
