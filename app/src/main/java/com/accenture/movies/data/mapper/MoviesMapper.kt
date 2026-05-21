package com.accenture.movies.data.mapper

import com.accenture.movies.data.dto.MovieDto
import com.accenture.movies.data.dto.MoviesResponseDto
import com.accenture.movies.domain.model.Movie


fun MoviesResponseDto.toDomainMovies(): List<Movie> {
    return movies?.map { movieDto -> movieDto.toDomainMovie() } ?: emptyList()
}

fun MovieDto.toDomainMovie() =
    Movie(title = title, year = year, imdbID = imdbID, type = type, poster = poster)