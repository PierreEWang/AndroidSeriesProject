package com.example.androidseriesproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidseriesproject.repository.TvShowRepository
import com.example.androidseriesproject.ui.UiState
import com.example.androidseriesproject.util.Resource
import com.example.androidseriesproject.model.TvShow
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

	private var currentPage = 1
	private var totalPages = 1
	private var isLoadingMore = false
	private val allShows = mutableListOf<TvShow>()

	init {
		fetchShows()
	}

	private fun fetchShows() {
		viewModelScope.launch {
			repository.getMostPopularShows(page = 1).collectLatest { result ->
				when (result) {
					is Resource.Loading -> {
						if (currentPage == 1) {
							_uiState.value = UiState.Loading
						}
					}
					is Resource.Success -> {
						val response = result.data
						currentPage = response.page
						totalPages = response.pages

						// Pour la première page, on remplace la liste
						if (response.page == 1) {
							allShows.clear()
							allShows.addAll(response.tvShows)
						} else {
							// Pour les pages suivantes, on ajoute à la liste existante
							allShows.addAll(response.tvShows)
						}

						_uiState.value = UiState.Success(
							shows = allShows.toList(),
							isLoadingMore = false,
							hasMorePages = currentPage < totalPages,
							currentPage = currentPage,
							totalPages = totalPages
						)
						isLoadingMore = false
					}
					is Resource.Error -> {
						if (currentPage == 1) {
							_uiState.value = UiState.Error(result.message)
						} else {
							// En cas d'erreur sur une page supplémentaire, on garde l'état actuel
							// mais on arrête le loading
							val currentState = _uiState.value
							if (currentState is UiState.Success) {
								_uiState.value = currentState.copy(isLoadingMore = false)
							}
						}
						isLoadingMore = false
					}
				}
			}
		}
	}

	fun loadMoreShows() {
		// Éviter les appels multiples en parallèle
		if (isLoadingMore) return

		val currentState = _uiState.value
		if (currentState is UiState.Success && currentState.hasMorePages) {
			isLoadingMore = true
			val nextPage = currentPage + 1

			// Mettre à jour l'état pour afficher le spinner de chargement
			_uiState.value = currentState.copy(isLoadingMore = true)

			viewModelScope.launch {
				repository.getMostPopularShows(page = nextPage).collectLatest { result ->
					when (result) {
						is Resource.Loading -> {
							// L'état de loading est déjà mis à jour ci-dessus
						}
						is Resource.Success -> {
							val response = result.data
							currentPage = response.page
							totalPages = response.pages

							// Ajouter les nouvelles séries à la liste existante
							allShows.addAll(response.tvShows)

							_uiState.value = UiState.Success(
								shows = allShows.toList(),
								isLoadingMore = false,
								hasMorePages = currentPage < totalPages,
								currentPage = currentPage,
								totalPages = totalPages
							)
							isLoadingMore = false
						}
						is Resource.Error -> {
							// En cas d'erreur, on garde l'état actuel mais on arrête le loading
							val state = _uiState.value
							if (state is UiState.Success) {
								_uiState.value = state.copy(isLoadingMore = false)
							}
							isLoadingMore = false
						}
					}
				}
			}
		}
	}

	fun retry() {
		currentPage = 1
		totalPages = 1
		isLoadingMore = false
		allShows.clear()
		fetchShows()
	}
}