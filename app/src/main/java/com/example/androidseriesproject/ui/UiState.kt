package com.example.androidseriesproject.ui

import com.example.androidseriesproject.model.TvShow


sealed class UiState {
	object Loading : UiState()
	data class Success(val shows: List<TvShow>) : UiState()
	data class Error(val message: String) : UiState()
}
