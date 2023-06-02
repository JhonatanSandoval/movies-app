package pro.jsandoval.moviesapp.movies

import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pro.jsandoval.movieapp.utils.base.BaseFragment
import pro.jsandoval.movieapp.utils.ext.value
import pro.jsandoval.movieapp.utils.mvvm.ext.subscribe
import pro.jsandoval.moviesapp.model.Movie
import pro.jsandoval.moviesapp.movies.databinding.FragmentMoviesBinding
import pro.jsandoval.moviesapp.navigation.MoviesDetailsNavigator
import timber.log.Timber
import javax.inject.Inject
import pro.jsandoval.moviesapp.resources.R as Resources

@AndroidEntryPoint
class MoviesFragment : BaseFragment<FragmentMoviesBinding>(FragmentMoviesBinding::inflate) {

    @Inject lateinit var moviesDetailsNavigator: MoviesDetailsNavigator

    private val viewModel: MoviesViewModel by viewModels<MoviesViewModelImpl>()

    private val moviesAdapter by lazy {
        MoviesAdapter(onMovieClicked = { movie -> viewModel.onMovieSelected(movie) })
    }

    override fun init() {
        initViews()
        observeLiveData()
        viewModel.onStart()
    }

    private fun initViews() = with(binding) {
        searchViewEditText.addTextChangedListener { checkFilteredText(searchViewEditText.value) }
        swipeRefreshLayout.setOnRefreshListener {
            searchViewEditText.setText(pro.jsandoval.moviesapp.resources.R.string.empty)
            viewModel.onRefresh()
        }
        setupRecyclerView()
    }

    private fun checkFilteredText(query: String) = with(binding) {
        moviesAdapter.filter.filter(query)
        Timber.d("adapter itemCount: ${moviesAdapter.itemCount}")
        if (moviesAdapter.itemCount == 0) {
            showError(getString(Resources.string.results_not_found_for_x, query))
        } else {
            showMoviesList()
        }
    }

    private fun showMoviesList() {
        binding.moviesList.isVisible = true
        binding.errorMessage.isVisible = false
    }

    private fun setupRecyclerView() = with(binding) {
        moviesList.adapter = moviesAdapter
    }

    private fun handleMovies(movies: List<Movie>) {
        moviesAdapter.setData(movies)
        showMoviesList()
    }

    private fun showError(message: String) {
        binding.errorMessage.text = message
        binding.errorMessage.isVisible = true
        binding.moviesList.isVisible = false
    }

    private fun observeLiveData() {
        subscribe(viewModel.errorMessage, ::showError)
        subscribe(viewModel.movies, ::handleMovies)
        subscribe(viewModel.movieIdSelected) { movieId -> moviesDetailsNavigator.launchMoviesDetails(movieId) }
        subscribe(viewModel.progressVisibility) { binding.swipeRefreshLayout.isRefreshing = it }
    }
}