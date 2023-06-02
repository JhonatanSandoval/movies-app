package pro.jsandoval.moviesapp.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import pro.jsandoval.movieapp.utils.mvvm.coroutines.IoDispatcher
import pro.jsandoval.movieapp.utils.mvvm.event.SingleLiveEvent
import pro.jsandoval.movieapp.utils.mvvm.ext.withBooleanLiveData
import pro.jsandoval.movieapp.utils.mvvm.viewmodel.WithErrorMessage
import pro.jsandoval.movieapp.utils.mvvm.viewmodel.WithProgressLiveData
import pro.jsandoval.movieapp.utils.resolver.StringResourceResolver
import pro.jsandoval.movieapp.utils.result.onError
import pro.jsandoval.movieapp.utils.result.onSuccess
import pro.jsandoval.movieapp.utils.utils.NetworkChecker
import pro.jsandoval.moviesapp.data.repository.MoviesRepository
import pro.jsandoval.moviesapp.model.Movie
import timber.log.Timber
import javax.inject.Inject
import pro.jsandoval.moviesapp.resources.R as Resources

abstract class MoviesViewModel : ViewModel(), WithProgressLiveData, WithErrorMessage {

    abstract val movies: LiveData<List<Movie>>
    abstract val movieIdSelected: LiveData<Long>

    abstract fun onStart()
    abstract fun onRefresh()
    abstract fun onMovieSelected(movie: Movie)
}

@HiltViewModel
class MoviesViewModelImpl @Inject constructor(
    private val networkChecker: NetworkChecker,
    private val moviesRepository: MoviesRepository,
    private val stringResourceResolver: StringResourceResolver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : MoviesViewModel() {

    override val movies = MutableLiveData<List<Movie>>()
    override val movieIdSelected = SingleLiveEvent<Long>()

    override val errorMessage = SingleLiveEvent<String>()
    override val progressVisibility = MutableLiveData<Boolean>()

    override fun onStart() {
        if (networkChecker.isNetworkAvailable()) {
            fetchMovies()
        } else {
            errorMessage.postValue(
                stringResourceResolver.getString(Resources.string.error_no_network_available)
            )
        }
    }

    private fun fetchMovies() = viewModelScope.launch(ioDispatcher) {
        moviesRepository.getPopularMovies()
            .withBooleanLiveData(progressVisibility)
            .collect { result ->
                result.onSuccess { moviesList ->
                    Timber.d("movies list: $moviesList")
                    movies.postValue(moviesList)
                }.onError { error -> errorMessage.postValue(error?.message) }
            }
    }

    override fun onRefresh() {
        fetchMovies()
    }

    override fun onMovieSelected(movie: Movie) {
        val movieId = movie.movieId
        Timber.d("movieId selected: $movieId")
        movieIdSelected.postValue(movieId)
    }
}