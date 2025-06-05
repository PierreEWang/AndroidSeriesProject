package com.example.androidseriesproject.data.repository

import com.example.androidseriesproject.model.TvShowResponse


interface TvShowRepository {
	suspend fun getPopularShows(page: Int = 1): TvShowResponse
}
