package com.example.androidseriesproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidseriesproject.repository.TvShowRepository
import com.example.androidseriesproject.ui.ShowDetailsUiState
import com.example.androidseriesproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowDetailsViewModel @Inject constructor(
    private val repository: TvShowRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShowDetailsUiState>(ShowDetailsUiState.Loading)
    val uiState: StateFlow<ShowDetailsUiState> = _uiState

    fun loadShowDetails(showId: Int) {
        viewModelScope.launch {
            _uiState.value = ShowDetailsUiState.Loading

            repository.getShowDetails(showId).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = ShowDetailsUiState.Loading
                    }
                    is Resource.Success -> {
                        _uiState.value = ShowDetailsUiState.Success(result.data)
                    }
                    is Resource.Error -> {
                        _uiState.value = ShowDetailsUiState.Error(result.message)
                    }
                }
            }
        }
    }
}