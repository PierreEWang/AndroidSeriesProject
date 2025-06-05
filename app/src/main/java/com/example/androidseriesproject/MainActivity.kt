package com.example.androidseriesproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidseriesproject.ui.ShowListScreen
import com.example.androidseriesproject.ui.ShowDetailsScreen
import com.example.androidseriesproject.ui.theme.AndroidSeriesProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSeriesProjectTheme {
                TvShowApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowApp() {
    // État pour gérer la navigation
    var selectedShowId by remember { mutableStateOf<Int?>(null) }

    if (selectedShowId == null) {
        // Afficher la liste des séries
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Séries Populaires",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { innerPadding ->
            ShowListScreen(
                modifier = Modifier.padding(innerPadding),
                onShowClick = { showId ->
                    selectedShowId = showId
                }
            )
        }
    } else {
        // Afficher les détails de la série sélectionnée
        ShowDetailsScreen(
            showId = selectedShowId!!,
            onBackPressed = {
                selectedShowId = null
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TvShowAppPreview() {
    AndroidSeriesProjectTheme {
        TvShowApp()
    }
}