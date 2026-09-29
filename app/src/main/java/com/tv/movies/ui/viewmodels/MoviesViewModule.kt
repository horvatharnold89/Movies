package com.tv.movies.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tv.movies.domain.Resource
import com.tv.movies.domain.model.Movie
import com.tv.movies.domain.usecases.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MoviesViewModule @Inject constructor(private val getMoviesUseCase: GetMoviesUseCase) :
    ViewModel() {


    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState = _searchQuery
        .map { it.trim() }
        // Too-short queries switch to Idle immediately; valid ones wait until the user
        // stops typing. Every new character restarts the wait.
        .debounce { query -> if (query.length < MIN_QUERY_LENGTH) 0L else SEARCH_DEBOUNCE_MS }
        .distinctUntilChanged() // Don't search if query hasn't changed
        // flatMapLatest cancels the in-flight search when a newer query arrives
        .flatMapLatest { query ->
            if (query.length < MIN_QUERY_LENGTH) {
                flowOf<UiState>(UiState.Idle)
            } else {
                flow<UiState> {
                    emit(UiState.Loading)
                    emit(search(query))
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    private suspend fun search(query: String): UiState =
        try {
            when (val results = getMoviesUseCase(query)) {
                is Resource.Error -> UiState.Error(results.message ?: "Unknown")
                Resource.Loading -> UiState.Loading
                is Resource.Success -> UiState.Success(results.data)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Unknown")
        }


    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val movies: List<Movie>) : UiState()
        data class Error(val message: String) : UiState()
    }

    fun getMovies(name: String = "Matrix"){
        _searchQuery.value = name
    }

    init {
        getMovies()
    }

    companion object {
        const val MIN_QUERY_LENGTH = 3
        const val SEARCH_DEBOUNCE_MS = 1000L
    }
}