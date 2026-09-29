package com.tv.movies.domain.usecases

import com.tv.movies.domain.Resource
import com.tv.movies.domain.model.Movie
import com.tv.movies.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {

    suspend operator fun invoke(name: String): Resource<List<Movie>> {
        return moviesRepository.getMovies(name)
    }
}