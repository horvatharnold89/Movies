package com.tv.movies.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponseDto(
    @SerialName("Search") val movies : List<MovieDto>? = null,
    @SerialName("totalResults") val totalResults : String? = null,
    @SerialName("Error") val error: String? = null,
    @SerialName("Response") val response : String
)

@Serializable
data class MovieDto(
    @SerialName("Title") val title : String,
    @SerialName("Year") val year : String,
    @SerialName("imdbID") val imdbID : String,
    @SerialName("Type") val type : String,
    @SerialName("Poster") val poster : String
)