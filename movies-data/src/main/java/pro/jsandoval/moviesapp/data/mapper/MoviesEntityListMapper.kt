package pro.jsandoval.moviesapp.data.mapper

import pro.jsandoval.movieapp.utils.result.DataToDomainMapper
import pro.jsandoval.moviesapp.data.local.entity.MovieEntity
import pro.jsandoval.moviesapp.model.Movie
import javax.inject.Inject

class MoviesEntityListMapper @Inject constructor(): DataToDomainMapper<List<MovieEntity>, List<Movie>> {

    override fun mapToDomain(input: List<MovieEntity>): List<Movie> {
        return input.map {  entity ->
            Movie(
                movieId = entity.movieId,
                posterPath = entity.posterPath,
                title = entity.title,
                releaseDate = entity.releaseDate,
            )
        }
    }
}