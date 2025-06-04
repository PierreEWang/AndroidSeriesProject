package com.example.androidseriesproject.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.androidseriesproject.viewmodel.ShowViewModel
import com.example.androidseriesproject.model.TvShow

@Composable
fun ShowListScreen(
	modifier: Modifier = Modifier,
	viewModel: ShowViewModel = hiltViewModel()
) {
	val state by viewModel.uiState.collectAsState()

	when (state) {
		is UiState.Loading -> Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			CircularProgressIndicator()
		}

		is UiState.Success -> {
			val shows = (state as UiState.Success).shows
			LazyVerticalGrid(
				columns = GridCells.Fixed(2),
				modifier = modifier.fillMaxSize()
			) {
				items(shows) { show ->
					Column(modifier = Modifier.padding(8.dp)) {
						Image(
							painter = rememberAsyncImagePainter(show.imageThumbnailPath),
							contentDescription = show.name,
							modifier = Modifier
								.fillMaxWidth()
								.aspectRatio(1f)
						)
						Text(show.name, style = MaterialTheme.typography.bodyMedium)
					}
				}
			}
		}

		is UiState.Error -> Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			Text((state as UiState.Error).message, color = MaterialTheme.colorScheme.error)
		}
	}
}
