package pro.jsandoval.moviesapp.favorites

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pro.jsandoval.movieapp.utils.base.BaseFragment
import pro.jsandoval.movieapp.utils.mvvm.ext.subscribe
import pro.jsandoval.movieapp.utils.ui.MarginItemDecoration
import pro.jsandoval.moviesapp.favorites.databinding.FragmentFavoritesBinding
import pro.jsandoval.moviesapp.model.Movie
import pro.jsandoval.moviesapp.navigation.MoviesDetailsNavigator
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    private val viewModel: FavoritesViewModel by viewModels<FavoritesViewModelImpl>()

    @Inject lateinit var moviesDetailsNavigator: MoviesDetailsNavigator

    private val favoritesAdapter by lazy {
        FavoritesAdapter(
            onMovieClicked = { movie -> viewModel.onMovieSelected(movie) }
        )
    }

    override fun init() {
        initViews()
        observeLiveData()
        viewModel.onStart()
    }

    private fun initViews() = with(binding) {
        moviesList.adapter = favoritesAdapter
        moviesList.addItemDecoration(
            MarginItemDecoration(spaceSize = 24)
        )
    }

    private fun handleMovies(movies: List<Movie>) {
        binding.errorMessage.isVisible = false
        binding.moviesList.isVisible = true
        favoritesAdapter.submitList(movies)
    }

    private fun showError(message: String) {
        binding.moviesList.isVisible = false
        binding.errorMessage.isVisible = true
        binding.errorMessage.text = message
    }

    private fun observeLiveData() {
        subscribe(viewModel.movies, ::handleMovies)
        subscribe(viewModel.errorMessage, ::showError)
        subscribe(viewModel.movieIdSelected) { movieId -> moviesDetailsNavigator.launchMoviesDetails(movieId) }
    }
}