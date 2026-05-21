package com.accenture.movies.data.repository

import com.accenture.movies.data.api.MoviesApi
import com.accenture.movies.data.mapper.toDomainMovies
import com.accenture.movies.domain.Resource
import com.accenture.movies.domain.model.Movie
import com.accenture.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(private val moviesApi: MoviesApi) :
    MoviesRepository {
    override suspend fun getMovies(name: String): Resource<List<Movie>> =
        withContext(Dispatchers.IO) {
            Result

            try {
                val response = moviesApi.searchMovies(name)
                if (response.error == null) {
                    // Map DTO to Domain Model here
                    val domainData = response.toDomainMovies()
                    Resource.Success(domainData)
                } else {
                    Resource.Error("API Error: ${response.error}")
                }
            } catch (e: Exception) {
                Resource.Error("Network Failure", e)
            }
        }
}