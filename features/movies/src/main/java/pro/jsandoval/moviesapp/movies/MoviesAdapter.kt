package pro.jsandoval.moviesapp.movies

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import pro.jsandoval.movieapp.utils.ext.inflater
import pro.jsandoval.moviesapp.model.Movie
import pro.jsandoval.moviesapp.movies.databinding.ItemMovieBinding
import pro.jsandoval.moviesapp.resources.R as Resources

class MoviesAdapter(
    private val onMovieClicked: (Movie) -> Unit,
) : ListAdapter<Movie, ItemMovieViewHolder>(MoviesDiffCallback), Filterable {

    private var completeMoviesList = mutableListOf<Movie>()

    fun setData(movies: List<Movie>) {
        this.completeMoviesList = movies.toMutableList()
        submitList(movies)
    }

    private val customFilter = object : Filter() {
        override fun performFiltering(query: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Movie>()
            if (query.isNullOrBlank()) {
                filteredList.addAll(completeMoviesList)
            } else {
                completeMoviesList.forEach { movieItem ->
                    if (movieItem.title.contains(query, ignoreCase = true)) {
                        filteredList.add(movieItem)
                    }
                }
            }
            return FilterResults().apply { values = filteredList }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val newMoviesList = results?.values as MutableList<Movie>
            submitList(newMoviesList)
        }
    }

    override fun getFilter(): Filter = customFilter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMovieViewHolder {
        val binding = ItemMovieBinding.inflate(parent.inflater(), parent, false)
        return ItemMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemMovieViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item, onMovieClicked)
    }
}

class ItemMovieViewHolder(
    private val binding: ItemMovieBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie, onMovieClicked: (Movie) -> Unit) = with(binding) {
        movieImage.load(movie.posterPath)
        movieTitle.text = movie.title

        val context = binding.root.context
        val releaseDate = context.getString(Resources.string.release_date_x, movie.releaseDateFull)
        movieReleaseDate.text = releaseDate

        movieRatingBar.rating = movie.voteAverage

        cardContainer.setOnClickListener { onMovieClicked.invoke(movie) }
    }
}

object MoviesDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.movieId == newItem.movieId && oldItem.releaseDate == newItem.releaseDate
}