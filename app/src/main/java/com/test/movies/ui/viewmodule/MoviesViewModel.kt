package com.test.movies.ui.viewmodule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.movies.domain.model.Movie
import com.test.movies.domain.repository.MoviesRepository
import com.test.movies.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesViewModel @Inject constructor(val getMoviesUseCase: GetMoviesUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    sealed class UiState {
        object Loading : UiState()
        class Success(val movies: List<Movie>) : UiState()
        class Error(val message: String, val throwable: Throwable) : UiState()
    }


    fun getMovies() {
        viewModelScope.launch {
            try {
                _uiState.emit(UiState.Success(getMoviesUseCase.invoke()))
            } catch (e: Exception){
                _uiState.emit(UiState.Error(e.message ?: "Something went wrong", e))
            }
        }
    }

}