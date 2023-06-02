package pro.jsandoval.moviesapp.moviesdetails

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
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
import pro.jsandoval.movieapp.utils.testing.data.FIRST_MOVIE_ID
import pro.jsandoval.movieapp.utils.testing.data.POPULAR_MOVIES
import pro.jsandoval.movieapp.utils.testing.getOrAwaitValue
import pro.jsandoval.movieapp.utils.utils.NetworkChecker
import pro.jsandoval.moviesapp.data.repository.MoviesRepository

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesDetailsViewModelTest : HasInstantExecutionRule, HasCoroutineExecutionRule {

    private val networkChecker = mock<NetworkChecker>()
    private val moviesRepository = mock<MoviesRepository>()
    private val stringResourceResolver = mock<StringResourceResolver>()

    private lateinit var viewModel: MoviesDetailsViewModel

    @Before
    fun setup() {
        viewModel = MoviesDetailsViewModelImpl(
            networkChecker = networkChecker,
            moviesRepository = moviesRepository,
            stringResourceResolver = stringResourceResolver,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `onStart with movieId loads movie details`() = runTest {
        // given
        whenever(networkChecker.isNetworkAvailable()).thenReturn(true)
        val movieId = FIRST_MOVIE_ID
        val movie = POPULAR_MOVIES.first()
        whenever(moviesRepository.getMoviesDetails(movieId))
            .thenReturn(flowOf(CustomResult.Success(movie)))

        // when
        viewModel.onStart(movieId)

        // then
        assertEquals(movie, viewModel.movie.getOrAwaitValue())
    }

    @Test
    fun `onStart shows error message when there is no internet connection`() = runTest {
        // given
        whenever(networkChecker.isNetworkAvailable()).thenReturn(false)
        val errorMessage = "No internet connection."
        whenever(stringResourceResolver.getString(any())).thenReturn(errorMessage)

        // when
        viewModel.onStart(FIRST_MOVIE_ID)

        // then
        assertEquals(errorMessage, viewModel.errorMessage.getOrAwaitValue())
    }

    @Test
    fun `onStart with movieId loads favorite movie`() = runTest {
        // given
        whenever(networkChecker.isNetworkAvailable()).thenReturn(true)
        val movieId = FIRST_MOVIE_ID
        val movie = POPULAR_MOVIES.first().copy(isFavorite = true)
        whenever(moviesRepository.getMoviesDetails(movieId))
            .thenReturn(flowOf(CustomResult.Success(movie)))

        // when
        viewModel.onStart(movieId)

        // then
        assertEquals(movie, viewModel.movie.getOrAwaitValue())
        assertEquals(true, viewModel.isFavoriteMovie.getOrAwaitValue())
    }

    @Test
    fun `onToggleFavorite save movie locally when tries to save it as favorite`() = runTest {
        // given
        whenever(networkChecker.isNetworkAvailable()).thenReturn(true)
        val movieId = FIRST_MOVIE_ID
        val movie = POPULAR_MOVIES.first()
        whenever(moviesRepository.getMoviesDetails(movieId))
            .thenReturn(flowOf(CustomResult.Success(movie)))

        // when
        viewModel.onStart(movieId)
        viewModel.onToggleFavorite()

        // then
        verify(moviesRepository).saveMovieLocally(movie)
    }

    @Test
    fun `onToggleFavorite delete the movie from database when tries to mark as not favorite`() = runTest {
        // given
        whenever(networkChecker.isNetworkAvailable()).thenReturn(true)
        val movieId = FIRST_MOVIE_ID
        val movie = POPULAR_MOVIES.first().copy(isFavorite = true)
        whenever(moviesRepository.getMoviesDetails(movieId))
            .thenReturn(flowOf(CustomResult.Success(movie)))

        // when
        viewModel.onStart(movieId)
        viewModel.onToggleFavorite()

        // then
        verify(moviesRepository).deleteMovie(movieId)
    }

    @Test
    fun `onBackPressed finishes the screen`() {
        // when
        viewModel.onBackPressed()

        // then
        assertEquals(Unit, viewModel.finish.getOrAwaitValue())
    }
}