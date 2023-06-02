package pro.jsandoval.moviesapp.movies

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import pro.jsandoval.movieapp.utils.resolver.StringResourceResolver
import pro.jsandoval.movieapp.utils.result.CustomResult
import pro.jsandoval.movieapp.utils.testing.HasCoroutineExecutionRule
import pro.jsandoval.movieapp.utils.testing.HasInstantExecutionRule
import pro.jsandoval.movieapp.utils.testing.data.POPULAR_MOVIES
import pro.jsandoval.movieapp.utils.testing.getOrAwaitValue
import pro.jsandoval.movieapp.utils.utils.NetworkChecker
import pro.jsandoval.moviesapp.data.repository.MoviesRepository

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModelTest : HasInstantExecutionRule, HasCoroutineExecutionRule {

    private val networkChecker = mock<NetworkChecker>()
    private val moviesRepository = mock<MoviesRepository>()
    private val stringResourceResolver = mock<StringResourceResolver>()

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setup() {
        viewModel = MoviesViewModelImpl(
            networkChecker = networkChecker,
            moviesRepository = moviesRepository,
            stringResourceResolver = stringResourceResolver,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `onStart loads movies successfully`() = runTest {
        // given
        mockMoviesList()

        // when
        viewModel.onStart()

        // then
        val expectedValue = POPULAR_MOVIES
        assertEquals(expectedValue, viewModel.movies.getOrAwaitValue())
    }

    @Test
    fun `onStart shows error message when there is no internet connection`() = runTest {
        // given
        whenever(networkChecker.isNetworkAvailable()).thenReturn(false)
        val errorMessage = "No internet connection."
        whenever(stringResourceResolver.getString(any())).thenReturn(errorMessage)

        // when
        viewModel.onStart()

        // then
        assertEquals(errorMessage, viewModel.errorMessage.getOrAwaitValue())
    }

    @Test
    fun `onRefresh reloads movies list`() {
        // given
        mockMoviesList()

        // when
        viewModel.onRefresh()

        // then
        val expectedValue = POPULAR_MOVIES
        assertEquals(expectedValue, viewModel.movies.getOrAwaitValue())
    }

    @Test
    fun `onMovieSelected loads the movies id`() {
        // given
        val movie = POPULAR_MOVIES.first()

        // when
        viewModel.onMovieSelected(movie)

        // then
        val expectedValue = movie.movieId
        assertEquals(expectedValue, viewModel.movieIdSelected.getOrAwaitValue())
    }

    private fun mockMoviesList() {
        whenever(networkChecker.isNetworkAvailable()).thenReturn(true)
        whenever(moviesRepository.getPopularMovies())
            .thenReturn(flowOf(CustomResult.Success(POPULAR_MOVIES)))
    }
}