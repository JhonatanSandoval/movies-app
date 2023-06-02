package pro.jsandoval.moviesapp.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import pro.jsandoval.movieapp.utils.mvvm.coroutines.IoDispatcher
import pro.jsandoval.movieapp.utils.mvvm.event.SingleLiveEvent
import pro.jsandoval.movieapp.utils.mvvm.viewmodel.WithErrorMessage
import pro.jsandoval.movieapp.utils.resolver.StringResourceResolver
import pro.jsandoval.moviesapp.data.repository.MoviesRepository
import pro.jsandoval.moviesapp.model.Movie
import timber.log.Timber
import javax.inject.Inject
import pro.jsandoval.moviesapp.resources.R as Resources

abstract class FavoritesViewModel : ViewModel(), WithErrorMessage {
    abstract val movies: LiveData<List<Movie>>
    abstract val movieIdSelected: LiveData<Long>

    abstract fun onStart()
    abstract fun onMovieSelected(movie: Movie)
}

@HiltViewModel
class FavoritesViewModelImpl @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val stringResourceResolver: StringResourceResolver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FavoritesViewModel() {

    override val movies = MutableLiveData<List<Movie>>()
    override val movieIdSelected = SingleLiveEvent<Long>()

    override val errorMessage = SingleLiveEvent<String>()

    override fun onStart() {
        getFavoritesMovies()
    }

    private fun getFavoritesMovies() = viewModelScope.launch(ioDispatcher) {
        moviesRepository.getFavoritesMovies().collect { moviesFound ->
            movies.postValue(moviesFound)
            if (moviesFound.isEmpty()) {
                errorMessage.postValue(stringResourceResolver.getString(Resources.string.error_no_favorites_saved_yet))
            }
        }
    }

    override fun onMovieSelected(movie: Movie) {
        val movieId = movie.movieId
        Timber.d("movieId selected: $movieId")
        movieIdSelected.postValue(movieId)
    }
}