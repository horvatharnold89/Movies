package com.tv.movies.ui

import com.tv.movies.domain.Resource
import com.tv.movies.domain.model.Movie
import com.tv.movies.domain.usecases.GetMoviesUseCase
import com.tv.movies.ui.viewmodels.MoviesViewModule
import com.tv.movies.ui.viewmodels.MoviesViewModule.Companion.SEARCH_DEBOUNCE_MS
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModelTest {

    private val getMoviesUseCase: GetMoviesUseCase = mockk()
    private lateinit var viewModel: MoviesViewModule

    private val movies = listOf(
        Movie(title = "Batman", year = "1989", poster = "poster_url", type = "movie", imdbID = "1")
    )

    @Before
    fun setUp() {
        // viewModelScope runs on Dispatchers.Main, so replace it with a virtual-time dispatcher
        Dispatchers.setMain(StandardTestDispatcher())
        coEvery { getMoviesUseCase(any()) } returns Resource.Success(movies)
        viewModel = MoviesViewModule(getMoviesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /** uiState only runs while it has a subscriber (WhileSubscribed). */
    private fun TestScope.subscribe() {
        backgroundScope.launch { viewModel.uiState.collect {} }
    }

    @Test
    fun `initial Matrix query is searched after the debounce`() = runTest {
        subscribe()

        advanceTimeBy(SEARCH_DEBOUNCE_MS - 1)
        runCurrent()
        coVerify(exactly = 0) { getMoviesUseCase(any()) }

        advanceTimeBy(1)
        runCurrent()
        coVerify(exactly = 1) { getMoviesUseCase("Matrix") }
        assertEquals(MoviesViewModule.UiState.Success(movies), viewModel.uiState.value)
    }

    @Test
    fun `query shorter than 3 characters shows Idle and does not search`() = runTest {
        subscribe()
        viewModel.getMovies("Ba")

        advanceTimeBy(SEARCH_DEBOUNCE_MS * 2)
        runCurrent()

        assertEquals(MoviesViewModule.UiState.Idle, viewModel.uiState.value)
        coVerify(exactly = 0) { getMoviesUseCase(any()) }
    }

    @Test
    fun `whitespace does not count towards the minimum length`() = runTest {
        subscribe()
        viewModel.getMovies("  Ba  ")

        advanceTimeBy(SEARCH_DEBOUNCE_MS * 2)
        runCurrent()

        assertEquals(MoviesViewModule.UiState.Idle, viewModel.uiState.value)
        coVerify(exactly = 0) { getMoviesUseCase(any()) }
    }

    @Test
    fun `typing quickly only searches for the last query`() = runTest {
        subscribe()

        viewModel.getMovies("Bat")
        advanceTimeBy(300)
        viewModel.getMovies("Batm")
        advanceTimeBy(300)
        viewModel.getMovies("Batman")
        advanceTimeBy(SEARCH_DEBOUNCE_MS)
        runCurrent()

        coVerify(exactly = 1) { getMoviesUseCase("Batman") }
        coVerify(exactly = 0) { getMoviesUseCase("Bat") }
        coVerify(exactly = 0) { getMoviesUseCase("Batm") }
        coVerify(exactly = 0) { getMoviesUseCase("Matrix") }
        assertEquals(MoviesViewModule.UiState.Success(movies), viewModel.uiState.value)
    }

    @Test
    fun `new character restarts the one second wait`() = runTest {
        subscribe()

        viewModel.getMovies("Batm")
        advanceTimeBy(SEARCH_DEBOUNCE_MS - 100)
        viewModel.getMovies("Batma")
        // Past the first query's deadline, but not the second's
        advanceTimeBy(SEARCH_DEBOUNCE_MS - 100)
        runCurrent()
        coVerify(exactly = 0) { getMoviesUseCase(any()) }

        advanceTimeBy(100)
        runCurrent()
        coVerify(exactly = 1) { getMoviesUseCase("Batma") }
    }
}