package com.test.movies.di

import com.test.movies.data.repositoryimpl.MoviesRepositoryImpl
import com.test.movies.domain.repository.MoviesRepository
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
    fun bindMoviesRepositoryImpl(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository

}