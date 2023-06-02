package pro.jsandoval.movieapp.utils.testing.data

import pro.jsandoval.moviesapp.model.Movie
import java.util.Date

const val FIRST_MOVIE_ID = 10001L
const val SECOND_MOVIE_ID = 10002L

val POPULAR_MOVIES = listOf(
    Movie(
        movieId = FIRST_MOVIE_ID,
        isForAdult = false,
        backdropPath = "",
        budget = 1000,
        genres = listOf(),
        homepageUrl = "",
        posterPath = "",
        language = "es",
        originalTitle = "Original Title Movie 1",
        title = "Title Movie 1",
        overview = "",
        releaseDate = Date(),
        voteAverage = 7.8f,
        isFavorite = false,
    ),
    Movie(
        movieId = SECOND_MOVIE_ID,
        isForAdult = false,
        backdropPath = "",
        budget = 2000,
        genres = listOf(),
        homepageUrl = "",
        posterPath = "",
        language = "es",
        originalTitle = "Original Title Movie 2",
        title = "Title Movie 2",
        overview = "",
        releaseDate = Date(),
        voteAverage = 9f,
        isFavorite = false,
    ),
)