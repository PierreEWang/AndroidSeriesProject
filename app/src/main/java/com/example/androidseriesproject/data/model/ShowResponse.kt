package com.example.androidseriesproject.data.model

data class ShowResponse(
	val total: Int,
	val page: Int,
	val tv_shows: List<Show>
)
