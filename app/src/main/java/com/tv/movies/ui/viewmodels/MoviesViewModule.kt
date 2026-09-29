package com.tv.movies.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tv.movies.domain.Resource
import com.tv.movies.domain.model.Movie
import com.tv.movies.domain.usecases.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MoviesViewModule @Inject constructor(private val getMoviesUseCase: GetMoviesUseCase) :
    ViewModel() {


    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
//
//    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
val uiState = _searchQuery
    .debounce(500L) // Wait for user to stop typing
    .distinctUntilChanged() // Don't search if query hasn't changed
    .flatMapLatest { query ->
        flow {
            emit(UiState.Loading)
            try {
                when (val results = getMoviesUseCase(query)){
                    is Resource.Error -> emit(UiState.Error(results.message ?: "Unknown"))
                    Resource.Loading -> emit(UiState.Loading)
                    is Resource.Success -> emit(UiState.Success(results.data))
                }
            } catch (e: Exception) {
                emit(UiState.Error(e.message ?: "Unknown"))
            }
        }
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )


    sealed class UiState {
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


}