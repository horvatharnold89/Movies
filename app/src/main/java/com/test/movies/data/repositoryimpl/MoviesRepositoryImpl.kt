package com.test.movies.data.repositoryimpl

import com.test.movies.data.api.MoviesApi
import com.test.movies.data.mapper.toMovies
import com.test.movies.domain.model.Movie
import com.test.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(val movieApi: MoviesApi) : MoviesRepository {
    override suspend fun getMovies(): List<Movie> = withContext(Dispatchers.IO) {
        movieApi.searchMovies("").toMovies()
    }
}