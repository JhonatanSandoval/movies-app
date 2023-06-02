package pro.jsandoval.moviesapp.model

import java.text.DateFormat
import java.util.Date

private const val GENRES_SEPARATOR = " • "

data class Movie(
    val movieId: Long,
    val isForAdult: Boolean = true,
    val backdropPath: String = "",
    val budget: Long = 0L,
    val genres: List<Genre> = listOf(),
    val homepageUrl: String = "",
    val posterPath: String = "",
    val language: String = "",
    val originalTitle: String = "",
    val title: String = "",
    val overview: String = "",
    val releaseDate: Date = Date(),
    val voteAverage: Float = 0f,
    var isFavorite: Boolean = false,
) {

    val genresFormatted: String
        get() = genres.joinToString(GENRES_SEPARATOR) { genre -> genre.name }

    val releaseDateFull: String
        get() = DateFormat.getDateInstance(DateFormat.FULL).format(releaseDate)

    val releaseDateShort: String
        get() = DateFormat.getDateInstance().format(releaseDate)
}