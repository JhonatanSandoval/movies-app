package pro.jsandoval.moviesapp.moviesdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import pro.jsandoval.movieapp.utils.mvvm.coroutines.IoDispatcher
import pro.jsandoval.movieapp.utils.mvvm.event.EmptySingleLiveEvent
import pro.jsandoval.movieapp.utils.mvvm.event.SingleLiveEvent
import pro.jsandoval.movieapp.utils.mvvm.ext.withBooleanLiveData
import pro.jsandoval.movieapp.utils.mvvm.viewmodel.WithErrorMessage
import pro.jsandoval.movieapp.utils.mvvm.viewmodel.WithFinishEvent
import pro.jsandoval.movieapp.utils.mvvm.viewmodel.WithProgressLiveData
import pro.jsandoval.movieapp.utils.resolver.StringResourceResolver
import pro.jsandoval.movieapp.utils.result.onError
import pro.jsandoval.movieapp.utils.result.onSuccess
import pro.jsandoval.movieapp.utils.utils.NetworkChecker
import pro.jsandoval.moviesapp.data.repository.MoviesRepository
import pro.jsandoval.moviesapp.model.Movie
import javax.inject.Inject
import pro.jsandoval.moviesapp.resources.R as Resources

abstract class MoviesDetailsViewModel : ViewModel(), WithProgressLiveData, WithFinishEvent, WithErrorMessage {

    abstract val movie: LiveData<Movie>
    abstract val isFavoriteMovie: LiveData<Boolean>

    abstract fun onStart(movieId: Long)
    abstract fun onBackPressed()
    abstract fun onToggleFavorite()
}

@HiltViewModel
class MoviesDetailsViewModelImpl @Inject constructor(
    private val networkChecker: NetworkChecker,
    private val moviesRepository: MoviesRepository,
    private val stringResourceResolver: StringResourceResolver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : MoviesDetailsViewModel() {

    override val movie = MutableLiveData<Movie>()
    override val isFavoriteMovie = MutableLiveData<Boolean>()

    override val errorMessage = SingleLiveEvent<String>()
    override val progressVisibility = MutableLiveData<Boolean>()
    override val finish = EmptySingleLiveEvent()

    override fun onStart(movieId: Long) {
        if (networkChecker.isNetworkAvailable()) {
            fetchMovieDetails(movieId)
        } else {
            errorMessage.postValue(
                stringResourceResolver.getString(Resources.string.error_no_network_available)
            )
        }
    }

    private fun fetchMovieDetails(movieId: Long) = viewModelScope.launch(ioDispatcher) {
        moviesRepository.getMoviesDetails(movieId)
            .withBooleanLiveData(progressVisibility)
            .collect { result ->
                result.onSuccess { movieFound ->
                    movie.postValue(movieFound)
                    isFavoriteMovie.postValue(movieFound.isFavorite)
                }.onError { error -> errorMessage.postValue(error?.message) }
            }
    }

    override fun onToggleFavorite() {
        movie.value?.let { movieSelected ->
            viewModelScope.launch(ioDispatcher) {
                val saveAsFavorite = movieSelected.isFavorite.not()
                if (saveAsFavorite) {
                    // save movie locally
                    moviesRepository.saveMovieLocally(movieSelected)
                } else {
                    // delete movie from database
                    moviesRepository.deleteMovie(movieSelected.movieId)
                }
                isFavoriteMovie.postValue(saveAsFavorite)
            }
        }
    }

    override fun onBackPressed() {
        finish.postCall()
    }
}