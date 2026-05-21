package com.accenture.movies.di

import com.accenture.movies.data.repository.MoviesRepositoryImpl
import com.accenture.movies.domain.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun getMoviesRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository

}