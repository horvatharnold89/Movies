package com.tv.movies.domain.repository

import com.tv.movies.domain.Resource
import com.tv.movies.domain.model.Movie

interface MoviesRepository {

    suspend fun getMovies(name: String): Resource<List<Movie>>

}