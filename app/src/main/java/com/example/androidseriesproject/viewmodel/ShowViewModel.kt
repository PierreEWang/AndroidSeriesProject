package com.example.androidseriesproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidseriesproject.repository.TvShowRepository
import com.example.androidseriesproject.model.TvShowResponse

import com.example.androidseriesproject.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
			try {
				val result = repository.getPopularShows()  // <-- ici le nom correct
				//_uiState.value = UiState.Success(result.tvShows) //TODO

			} catch (e: Exception) {
				_uiState.value = UiState.Error("Erreur : ${e.localizedMessage}")
			}
		}
	}

}


