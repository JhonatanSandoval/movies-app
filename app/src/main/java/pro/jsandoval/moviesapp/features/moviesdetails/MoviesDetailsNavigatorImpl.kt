package pro.jsandoval.moviesapp.features.moviesdetails

import android.app.Activity
import pro.jsandoval.moviesapp.moviesdetails.MoviesDetailsActivity
import pro.jsandoval.moviesapp.navigation.MoviesDetailsNavigator
import javax.inject.Inject

class MoviesDetailsNavigatorImpl @Inject constructor(
    private val activity: Activity,
) : MoviesDetailsNavigator {

    override fun launchMoviesDetails(movieId: Long) {
        MoviesDetailsActivity.createIntent(activity, movieId).also { intent ->
            activity.startActivity(intent)
        }
    }
}