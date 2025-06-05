package com.example.androidseriesproject.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.androidseriesproject.viewmodel.ShowViewModel
import com.example.androidseriesproject.model.TvShow

@Composable
fun ShowListScreen(
	modifier: Modifier = Modifier,
	viewModel: ShowViewModel = hiltViewModel()
) {
	val state by viewModel.uiState.collectAsState()

	when (state) {
		is UiState.Loading -> {
			Box(
				modifier = modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				Column(horizontalAlignment = Alignment.CenterHorizontally) {
					CircularProgressIndicator()
					Spacer(modifier = Modifier.height(16.dp))
					Text(
						text = "Chargement des séries...",
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
		}

		is UiState.Success -> {
			val shows = (state as UiState.Success).shows
			LazyVerticalGrid(
				columns = GridCells.Fixed(2),
				modifier = modifier.fillMaxSize(),
				contentPadding = PaddingValues(8.dp),
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				items(shows) { show ->
					TvShowItem(show = show)
				}
			}
		}

		is UiState.Error -> {
			Box(
				modifier = modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				Column(horizontalAlignment = Alignment.CenterHorizontally) {
					Text(
						text = "Erreur de chargement",
						style = MaterialTheme.typography.titleMedium,
						color = MaterialTheme.colorScheme.error
					)
					Spacer(modifier = Modifier.height(8.dp))
					Text(
						text = (state as UiState.Error).message,
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.error,
						textAlign = TextAlign.Center
					)
				}
			}
		}
	}
}

@Composable
fun TvShowItem(
	show: TvShow,
	modifier: Modifier = Modifier
) {
	Card(
		modifier = modifier.fillMaxWidth(),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
		shape = RoundedCornerShape(8.dp)
	) {
		Column {
			// Image de la série avec Coil
			SubcomposeAsyncImage(
				model = show.imageThumbnailPath,
				contentDescription = show.name,
				modifier = Modifier
					.fillMaxWidth()
					.aspectRatio(0.75f) // Ratio typique pour les posters de séries
					.clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
				contentScale = ContentScale.Crop,
				loading = {
					Box(
						modifier = Modifier.fillMaxSize(),
						contentAlignment = Alignment.Center
					) {
						CircularProgressIndicator(
							modifier = Modifier.size(32.dp),
							strokeWidth = 2.dp
						)
					}
				},
				error = {
					Box(
						modifier = Modifier.fillMaxSize(),
						contentAlignment = Alignment.Center
					) {
						Column(horizontalAlignment = Alignment.CenterHorizontally) {
							Icon(
								painter = painterResource(android.R.drawable.ic_menu_report_image),
								contentDescription = "Image non disponible",
								tint = MaterialTheme.colorScheme.error
							)
							Text(
								text = "Image\nindisponible",
								style = MaterialTheme.typography.bodySmall,
								textAlign = TextAlign.Center,
								color = MaterialTheme.colorScheme.error
							)
						}
					}
				}
			)

			// Informations de la série
			Column(
				modifier = Modifier.padding(12.dp)
			) {
				Text(
					text = show.name,
					style = MaterialTheme.typography.titleSmall,
					fontWeight = FontWeight.Bold,
					maxLines = 2,
					overflow = TextOverflow.Ellipsis
				)

				Spacer(modifier = Modifier.height(4.dp))

				Text(
					text = show.network,
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)

				if (show.status.isNotEmpty()) {
					Spacer(modifier = Modifier.height(2.dp))
					Text(
						text = show.status,
						style = MaterialTheme.typography.bodySmall,
						color = if (show.status.lowercase().contains("running")) {
							MaterialTheme.colorScheme.primary
						} else {
							MaterialTheme.colorScheme.onSurfaceVariant
						},
						maxLines = 1,
						overflow = TextOverflow.Ellipsis
					)
				}
			}
		}
	}
}