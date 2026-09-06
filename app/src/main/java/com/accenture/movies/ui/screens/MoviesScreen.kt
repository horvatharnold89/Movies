package com.accenture.movies.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.accenture.movies.domain.model.Movie
import com.accenture.movies.ui.viewmodels.MoviesViewModule

@Composable
fun MoviesScreen(viewModel: MoviesViewModule = hiltViewModel(), innerPadding: PaddingValues) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)) {
        // Search TextField
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.getMovies(it) },
            label = { Text("Search items...") },
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            singleLine = true
        )

        when (val state = uiState) {
            is MoviesViewModule.UiState.Error -> SetLabel(state.message)
            is MoviesViewModule.UiState.Loading -> SetLabel("Loading...")
            is MoviesViewModule.UiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(state.movies) { movie ->
                        MovieItem(movie)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie) {
    Card(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
    ) {
        SetLabel(movie.title)
        SetLabel(movie.year)
    }
}

@Composable
fun SetLabel(text: String) {
    Text(
        text = text,
        modifier = Modifier
    )
}