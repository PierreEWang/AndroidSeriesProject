package com.example.androidseriesproject.data.repository

import com.example.androidseriesproject.data.model.ShowResponse

interface ShowRepository {
	suspend fun getPopularShows(page: Int = 1): ShowResponse
}
