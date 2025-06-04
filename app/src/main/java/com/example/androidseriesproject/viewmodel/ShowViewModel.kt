package com.example.androidseriesproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidseriesproject.data.repository.ShowRepository
import com.example.androidseriesproject.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
	private val repository: ShowRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
	val uiState: StateFlow<UiState> = _uiState

	init {
		fetchShows()
	}

	private fun fetchShows() {
		viewModelScope.launch {
			try {
				val result = repository.getPopularShows()
				_uiState.value = UiState.Success(result.tv_shows)
			} catch (e: Exception) {
				_uiState.value = UiState.Error("Erreur : ${e.localizedMessage}")
			}
		}
	}
}
