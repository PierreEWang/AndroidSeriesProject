package com.example.androidseriesproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidseriesproject.repository.TvShowRepository
import com.example.androidseriesproject.ui.UiState
import com.example.androidseriesproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
	private val repository: TvShowRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
	val uiState: StateFlow<UiState> = _uiState

	init {
		fetchShows()
	}

	private fun fetchShows() {
		viewModelScope.launch {
			repository.getMostPopularShows().collectLatest { result ->
				when (result) {
					is Resource.Loading -> {
						_uiState.value = UiState.Loading
					}
					is Resource.Success -> {
						_uiState.value = UiState.Success(result.data.tvShows)
					}
					is Resource.Error -> {
						_uiState.value = UiState.Error(result.message)
					}
				}
			}
		}
	}
}