package com.example.androidseriesproject.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.androidseriesproject.model.TvShowDetails
import com.example.androidseriesproject.viewmodel.ShowDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDetailsScreen(
    showId: Int,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShowDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(showId) {
        viewModel.loadShowDetails(showId)
    }

    when (uiState) {
        is ShowDetailsUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Chargement des détails...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        is ShowDetailsUiState.Success -> {
            val showDetails = (uiState as ShowDetailsUiState.Success).details
            ShowDetailsContent(
                showDetails = showDetails,
                onBackPressed = onBackPressed,
                modifier = modifier
            )
        }

        is ShowDetailsUiState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Erreur de chargement",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = (uiState as ShowDetailsUiState.Error).message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.loadShowDetails(showId) }
                    ) {
                        Text("Réessayer")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onBackPressed
                    ) {
                        Text("Retour")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowDetailsContent(
    showDetails: TvShowDetails,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        // Header avec image et overlay
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                // Image de fond
                SubcomposeAsyncImage(
                    model = showDetails.getImageUrl(),
                    contentDescription = showDetails.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(android.R.drawable.ic_menu_report_image),
                                contentDescription = "Image non disponible",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                )

                // Overlay gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                )

                // Bouton retour
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(
                            Color.Black.copy(alpha = 0.5f),
                            RoundedCornerShape(50)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Retour",
                        tint = Color.White
                    )
                }

                // Titre et informations de base
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = showDetails.getDisplayName(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (showDetails.hasRating()) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Note",
                                tint = Color.Yellow,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = showDetails.getDisplayRating(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                        }

                        Text(
                            text = showDetails.getDisplayStatus(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Informations détaillées
        item {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Description
                if (showDetails.hasDescription()) {
                    Text(
                        text = "Synopsis",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = showDetails.getDisplayDescription(),
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Informations générales
                InfoSection(title = "Informations") {
                    InfoRow("Réseau", showDetails.getDisplayNetwork())
                    InfoRow("Pays", showDetails.getDisplayCountry())
                    InfoRow("Date de début", showDetails.getDisplayStartDate())
                    if (showDetails.hasEndDate()) {
                        InfoRow("Date de fin", showDetails.getDisplayEndDate())
                    }
                    InfoRow("Durée", showDetails.getDisplayRuntime())
                    if (showDetails.getDisplayRatingCount().isNotEmpty()) {
                        InfoRow("Nombre de votes", showDetails.getDisplayRatingCount())
                    }
                }

                // Genres
                val genres = showDetails.getDisplayGenres()
                if (genres.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Genres",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(genres) { genre ->
                            AssistChip(
                                onClick = { },
                                label = { Text(genre) }
                            )
                        }
                    }
                }

                // Images additionnelles
                val pictures = showDetails.getDisplayPictures()
                if (pictures.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Images",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(pictures) { imageUrl ->
                            SubcomposeAsyncImage(
                                model = imageUrl,
                                contentDescription = "Image de ${showDetails.getDisplayName()}",
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                                loading = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                // Épisodes (afficher les 5 derniers épisodes)
                showDetails.episodes?.let { episodes ->
                    if (episodes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Épisodes récents",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Prendre les 5 derniers épisodes
                        episodes.takeLast(5).forEach { episode ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text(
                                        text = "S${episode.season}E${episode.episode} - ${episode.getDisplayName()}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = episode.getDisplayAirDate(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // Espace en bas
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f, false)
        )
    }
}

// États pour l'écran de détails
sealed class ShowDetailsUiState {
    object Loading : ShowDetailsUiState()
    data class Success(val details: TvShowDetails) : ShowDetailsUiState()
    data class Error(val message: String) : ShowDetailsUiState()
}