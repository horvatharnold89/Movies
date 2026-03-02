package com.accenture.movies

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {
    companion object {
        private const val API_KEY = "411f9106"
    }

    @GET("/")
    suspend fun searchMovies(@Query("s") title: String?, @Query("apikey") apiKey: String = API_KEY): MovieSearchResponse
}

@Serializable
data class Movie(
    @SerialName("Title") val title : String? = null,
    @SerialName("Year") val year : String? = null,
    @SerialName("imdbID") val imdbID : String? = null,
    @SerialName("Type") val type : String? = null,
    @SerialName("Poster") val poster : String? = null
)

@Serializable
data class MovieSearchResponse(
    @SerialName("Search") val movies : List<Movie>? = null,
    @SerialName("totalResults") val totalResults : String? = null,
    @SerialName("Error") val error: String? = null,
    @SerialName("Response") val response : String
)
