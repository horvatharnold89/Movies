package com.accenture.movies.domain.repository

import com.accenture.movies.domain.Resource
import com.accenture.movies.domain.model.Movie

interface MoviesRepository {

    suspend fun getMovies(name: String): Resource<List<Movie>>

}