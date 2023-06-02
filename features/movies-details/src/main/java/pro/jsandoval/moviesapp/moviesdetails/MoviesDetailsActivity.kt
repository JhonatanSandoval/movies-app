package pro.jsandoval.moviesapp.moviesdetails

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import pro.jsandoval.movieapp.moviesdetails.databinding.ActivityMoviesDetailsBinding
import pro.jsandoval.movieapp.utils.base.BaseActivity
import pro.jsandoval.movieapp.utils.ext.requireExtrasOrFinish
import pro.jsandoval.movieapp.utils.mvvm.ext.subscribe
import pro.jsandoval.movieapp.utils.mvvm.viewmodel.subscribeFinishEvent
import pro.jsandoval.moviesapp.model.Movie
import pro.jsandoval.moviesapp.resources.R as Resources

const val MOVIE_ID_PARAM = "movieId"

@AndroidEntryPoint
class MoviesDetailsActivity : BaseActivity<ActivityMoviesDetailsBinding>(ActivityMoviesDetailsBinding::inflate) {

    private val viewModel: MoviesDetailsViewModel by viewModels<MoviesDetailsViewModelImpl>()

    override fun init() {
        initViews()
        observeLiveData()
        requireExtrasOrFinish(MOVIE_ID_PARAM) { bundle ->
            viewModel.onStart(movieId = bundle.getLong(MOVIE_ID_PARAM))
        }
    }

    private fun initViews() = with(binding) {
        movieFavorite.setOnClickListener { viewModel.onToggleFavorite() }
    }

    private fun loadMovieDetails(movie: Movie) = with(binding) {
        errorMessage.isVisible = false
        container.isVisible = true

        movieBackdrop.load(movie.backdropPath)
        movieImage.load(movie.posterPath)
        movieTitle.text = movie.title
        movieReleaseDate.text =
            getString(pro.jsandoval.moviesapp.resources.R.string.release_date_x, movie.releaseDateFull)
        movieRatingBar.rating = movie.voteAverage
        movieGenres.text = movie.genresFormatted
        movieDescription.text = movie.overview
    }

    private fun handleFavoriteIndicator(isFavorite: Boolean) = with(binding) {
        val resourceColor = if (isFavorite) Resources.color.red else Resources.color.light_gray
        val color = ContextCompat.getColor(this@MoviesDetailsActivity, resourceColor)
        movieFavorite.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    private fun showError(message: String) = with(binding) {
        errorMessage.isVisible = true
        errorMessage.text = message
        container.isVisible = false
    }

    private fun observeLiveData() {
        subscribeFinishEvent(viewModel)
        subscribe(viewModel.movie, ::loadMovieDetails)
        subscribe(viewModel.isFavoriteMovie, ::handleFavoriteIndicator)
        subscribe(viewModel.progressVisibility) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.container.isVisible = isLoading.not()
        }
        subscribe(viewModel.errorMessage, ::showError)
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }

    companion object {

        @JvmStatic
        fun createIntent(context: Context, movieId: Long): Intent {
            return Intent(context, MoviesDetailsActivity::class.java).apply {
                putExtra(MOVIE_ID_PARAM, movieId)
            }
        }
    }
}