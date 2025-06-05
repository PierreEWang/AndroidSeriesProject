package com.example.androidseriesproject.ui

import com.example.androidseriesproject.model.TvShow

sealed class UiState {
	object Loading : UiState()
	data class Success(
		val shows: List<TvShow>,
		val isLoadingMore: Boolean = false,
		val hasMorePages: Boolean = true,
		val currentPage: Int = 1,
		val totalPages: Int = 1
	) : UiState()
	data class Error(val message: String) : UiState()
}