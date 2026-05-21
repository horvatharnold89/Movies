package com.accenture.movies.domain.usecase

import com.accenture.movies.domain.model.Movie
import com.accenture.movies.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {

    suspend operator fun invoke(name: String): List<Movie>{
        return moviesRepository.getMovies(name)
    }
}