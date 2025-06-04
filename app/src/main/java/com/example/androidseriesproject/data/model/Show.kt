package com.example.androidseriesproject.data.model

data class Show(
	val id: Int,
	val name: String,
	val permalink: String,
	val start_date: String?,
	val country: String?,
	val network: String?,
	val status: String?,
	val image_thumbnail_path: String?
)
