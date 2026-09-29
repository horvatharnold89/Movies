package com.tv.movies.data.api

import com.tv.movies.data.dto.MoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    companion object {
        private const val API_KEY = "411f9106"
    }

    @GET("/")
    suspend fun searchMovies(@Query("s") title: String?, @Query("apikey") apiKey: String = API_KEY): MoviesResponseDto
}