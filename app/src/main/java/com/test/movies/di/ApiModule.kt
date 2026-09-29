package com.test.movies.di

import com.test.movies.data.api.MoviesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(Json.asConverterFactory("application/json; charset=utf-8".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesApi(retrofit: Retrofit) : MoviesApi{
        return retrofit.create(MoviesApi::class.java)
    }

}