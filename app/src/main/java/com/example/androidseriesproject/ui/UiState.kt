package com.example.androidseriesproject.ui

import com.example.androidseriesproject.data.model.Show

sealed class UiState {
	object Loading : UiState()
	data class Success(val shows: List<Show>) : UiState()
	data class Error(val message: String) : UiState()
}
