package pro.jsandoval.moviesapp.favorites

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import pro.jsandoval.movieapp.utils.ext.inflater
import pro.jsandoval.moviesapp.favorites.databinding.ItemMovieFavoriteBinding
import pro.jsandoval.moviesapp.model.Movie

class FavoritesAdapter(
    private val onMovieClicked: (Movie) -> Unit,
) : ListAdapter<Movie, ItemFavoriteViewHolder>(FavoritesDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFavoriteViewHolder {
        val binding = ItemMovieFavoriteBinding.inflate(parent.inflater(), parent, false)
        return ItemFavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemFavoriteViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item, onMovieClicked)
    }
}

class ItemFavoriteViewHolder(
    private val binding: ItemMovieFavoriteBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie, onMovieClicked: (Movie) -> Unit) = with(binding) {
        movieImage.load(movie.posterPath)
        movieTitle.text = movie.title

        val releaseDate = movie.releaseDateShort
        movieReleaseDate.text = releaseDate

        movieImage.setOnClickListener { onMovieClicked.invoke(movie) }
    }
}

object FavoritesDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.movieId == newItem.movieId && oldItem.releaseDate == newItem.releaseDate
}