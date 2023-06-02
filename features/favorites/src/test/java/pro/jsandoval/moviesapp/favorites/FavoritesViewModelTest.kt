package pro.jsandoval.moviesapp.favorites

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pro.jsandoval.movieapp.utils.resolver.StringResourceResolver
import pro.jsandoval.movieapp.utils.testing.HasCoroutineExecutionRule
import pro.jsandoval.movieapp.utils.testing.HasInstantExecutionRule
import pro.jsandoval.movieapp.utils.testing.data.POPULAR_MOVIES
import pro.jsandoval.movieapp.utils.testing.getOrAwaitValue
import pro.jsandoval.moviesapp.data.repository.MoviesRepository

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest : HasInstantExecutionRule, HasCoroutineExecutionRule {

    private val moviesRepository = mock<MoviesRepository>()
    private val stringResourceResolver = mock<StringResourceResolver>()

    private lateinit var viewModel: FavoritesViewModel

    @Before
    fun setup() {
        viewModel = FavoritesViewModelImpl(
            moviesRepository = moviesRepository,
            stringResourceResolver = stringResourceResolver,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `onStart loads favorites movies`() = runTest {
        // given
        val favoriteMovies = POPULAR_MOVIES
        whenever(moviesRepository.getFavoritesMovies())
            .thenReturn(flowOf(favoriteMovies))

        // when
        viewModel.onStart()

        // then
        assertEquals(favoriteMovies, viewModel.movies.getOrAwaitValue())
    }

    @Test
    fun `onStart show error message when there are no movies saved`() {
        // given
        whenever(moviesRepository.getFavoritesMovies()).thenReturn(flowOf(listOf()))
        val errorMessage = "No movies found"
        whenever(stringResourceResolver.getString(any())).thenReturn(errorMessage)

        // when
        viewModel.onStart()

        // then
        assertTrue(viewModel.movies.getOrAwaitValue().isEmpty())
        assertEquals(errorMessage, viewModel.errorMessage.getOrAwaitValue())
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
}