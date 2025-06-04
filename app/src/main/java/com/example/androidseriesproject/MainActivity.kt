package com.example.androidseriesproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.androidseriesproject.model.TvShow
import com.example.androidseriesproject.repository.TvShowRepository
import com.example.androidseriesproject.ui.theme.AndroidSeriesProjectTheme
import com.example.androidseriesproject.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.androidseriesproject.ui.ShowListScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var tvShowRepository: TvShowRepository
    
    private val tag = "MainActivity"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSeriesProjectTheme {
                // State to track loading status and TV shows
                var uiState by remember {
                    mutableStateOf<UiState>(UiState.Loading)
                }
                
                // Fetch TV shows when the UI is created
                testTvShowRepository(onStateChanged = { newState ->
                    // Update UI on the main thread
                    runOnUiThread {
                        uiState = newState
                    }
                })
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (uiState) {
                        is UiState.Loading -> {
                            Text(
                                text = "Loading TV shows...",
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        is UiState.Success -> {
                            TvShowList(
                                shows = (uiState as UiState.Success).shows,
                                modifier = Modifier.padding(innerPadding)
//                                ShowListScreen(modifier = Modifier.padding(innerPadding))
                            )
                        }
                        is UiState.Error -> {
                            Text(
                                text = "Error: ${(uiState as UiState.Error).message}",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
    
    // UI state sealed class to represent different states
    sealed class UiState {
        data object Loading : UiState()
        data class Success(val shows: List<TvShow>) : UiState()
        data class Error(val message: String) : UiState()
    }
    
    private fun testTvShowRepository(onStateChanged: (UiState) -> Unit) {
        lifecycleScope.launch {
            tvShowRepository.getMostPopularShows().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d(tag, "Loading TV shows...")
                        onStateChanged(UiState.Loading)
                    }
                    is Resource.Success -> {
                        val shows = result.data
                        Log.d(tag, "Successfully loaded ${shows.tvShows.size} TV shows")
                        Log.d(tag, "Page: ${shows.page}, Total pages: ${shows.pages}, Total: ${shows.total}")
                        
                        // Log the first few shows
                        shows.tvShows.take(3).forEach { show ->
                            Log.d(tag, "Show: ${show.name}, ID: ${show.id}, Network: ${show.network}")
                        }
                        
                        // Update UI state with the loaded shows
                        onStateChanged(UiState.Success(shows.tvShows))
                    }
                    is Resource.Error -> {
                        Log.e(tag, "Error loading TV shows: ${result.message}")
                        result.exception?.let {
                            Log.e(tag, "Exception: ${it.message}", it)
                        }
                        onStateChanged(UiState.Error(result.message))
                    }
                }
            }
        }
    }
}

@Composable
fun TvShowList(shows: List<TvShow>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(shows) { show ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = show.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Network: ${show.network}")
                    Text(text = "Status: ${show.status}")
                    Text(text = "Country: ${show.country}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidSeriesProjectTheme {
        Text("Android Series Project")
    }
}
