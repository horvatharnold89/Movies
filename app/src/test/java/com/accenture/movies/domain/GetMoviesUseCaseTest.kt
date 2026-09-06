package com.accenture.movies.domain

import com.accenture.movies.domain.model.Movie
import com.accenture.movies.domain.repository.MoviesRepository
import com.accenture.movies.domain.usecases.GetMoviesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

// Enables experimental coroutine testing APIs (runTest)
@OptIn(ExperimentalCoroutinesApi::class)
class GetMoviesUseCaseTest {

    // Creates a mock implementation of MoviesRepository.
    // No real network/database code will be executed.
    private val moviesRepository: MoviesRepository = mockk()

    // The class we want to test.
    private lateinit var getMoviesUseCase: GetMoviesUseCase

    // Runs before every test method.
    // Creates a fresh instance of the use case with the mocked repository.
    @Before
    fun setUp() {
        getMoviesUseCase = GetMoviesUseCase(moviesRepository)
    }

    @Test
    fun `invoke returns success when repository returns movies`() = runTest {

        // -------------------------
        // GIVEN
        // Arrange the test
        // -------------------------

        // Input parameter for the use case
        val query = "Batman"

        // Fake data that we expect the repository to return
        val movies = listOf(
            Movie(
                title = "Batman Begins",
                year = "2005",
                poster = "poster_url",
                type = "something",
                imdbID = "1111"
            )
        )

        // Whenever getMovies("Batman") is called,
        // return Resource.Success(movies) instead of calling real code.
        coEvery {
            moviesRepository.getMovies(query)
        } returns Resource.Success(movies)

        // -------------------------
        // WHEN
        // Execute the code under test
        // -------------------------

        val result = getMoviesUseCase(query)

        // -------------------------
        // THEN
        // Verify the result
        // -------------------------

        // Verify the returned object is Resource.Success
        assertTrue(result is Resource.Success)

        // Verify the data inside the success object
        if (result is Resource.Success) {
            assertEquals(movies, result.data)
        }

        // Verify that the repository method
        // was called exactly one time.
        coVerify(exactly = 1) {
            moviesRepository.getMovies(query)
        }
    }

    @Test
    fun `invoke returns error when repository returns error`() = runTest {

        // -------------------------
        // GIVEN
        // -------------------------

        val query = "Batman"
        val errorMessage = "Network error"

        // Mock repository to return an error
        coEvery {
            moviesRepository.getMovies(query)
        } returns Resource.Error(errorMessage)

        // -------------------------
        // WHEN
        // -------------------------

        val result = getMoviesUseCase(query)

        // -------------------------
        // THEN
        // -------------------------

        // Verify that the result is an Error
        assertTrue(result is Resource.Error)

        // Verify the error message
        if (result is Resource.Error) {
            assertEquals(errorMessage, result.message)
        }

        // Verify repository interaction
        coVerify(exactly = 1) {
            moviesRepository.getMovies(query)
        }
    }
}