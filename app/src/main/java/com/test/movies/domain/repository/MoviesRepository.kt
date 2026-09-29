package com.test.movies.domain.repository

import com.test.movies.domain.model.Movie

interface MoviesRepository {

    suspend fun getMovies(): List<Movie>
}