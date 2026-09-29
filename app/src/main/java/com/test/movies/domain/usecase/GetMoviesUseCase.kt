package com.test.movies.domain.usecase

import com.test.movies.domain.model.Movie
import com.test.movies.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(val moviesRepository: MoviesRepository) {

    suspend operator fun invoke(): List<Movie>{
        return moviesRepository.getMovies()
    }

}