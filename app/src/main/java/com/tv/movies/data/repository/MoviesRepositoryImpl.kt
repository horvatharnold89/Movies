package com.tv.movies.data.repository

import com.tv.movies.data.api.MoviesApi
import com.tv.movies.data.mapper.toDomainMovies
import com.tv.movies.domain.Resource
import com.tv.movies.domain.model.Movie
import com.tv.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(private val moviesApi: MoviesApi) :
    MoviesRepository {
    override suspend fun getMovies(name: String): Resource<List<Movie>> =
        withContext(Dispatchers.IO) {
            try {
                val response = moviesApi.searchMovies(name)
                if (response.error == null) {
                    // Map DTO to Domain Model here
                    val domainData = response.toDomainMovies()
                    Resource.Success(domainData)
                } else {
                    Resource.Error("API Error: ${response.error}")
                }
            } catch (e: CancellationException) {
                // A newer search replaced this one; don't report it as a failure
                throw e
            } catch (e: Exception) {
                Resource.Error("Network Failure", e)
            }
        }
}