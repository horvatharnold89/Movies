package com.tv.movies.data.mapper

import com.tv.movies.data.dto.MovieDto
import com.tv.movies.data.dto.MoviesResponseDto
import com.tv.movies.domain.model.Movie


fun MoviesResponseDto.toDomainMovies(): List<Movie> {
    return movies?.map { movieDto -> movieDto.toDomainMovie() } ?: emptyList()
}

fun MovieDto.toDomainMovie() =
    Movie(title = title, year = year, imdbID = imdbID, type = type, poster = poster)