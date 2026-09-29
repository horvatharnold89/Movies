package com.tv.movies.di

import com.tv.movies.data.repository.MoviesRepositoryImpl
import com.tv.movies.domain.repository.MoviesRepository
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