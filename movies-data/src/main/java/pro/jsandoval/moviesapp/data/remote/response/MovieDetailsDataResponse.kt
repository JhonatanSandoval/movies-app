package pro.jsandoval.moviesapp.data.remote.response

import com.google.gson.annotations.SerializedName
import pro.jsandoval.moviesapp.model.Genre
import pro.jsandoval.moviesapp.model.Movie
import java.util.Date

private const val BASE_IMAGES_PATH = "https://image.tmdb.org/t/p/"

data class MovieDetailsDataResponse(
    @SerializedName("adult") val isForAdult: Boolean?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("budget") val budget: Long?,
    @SerializedName("genres") val genres: List<GenreResponse>?,
    @SerializedName("homepage") val homepageUrl: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("id") val movieId: Long?,
    @SerializedName("original_language") val language: String?,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("release_date") val releaseDate: Date?,
    @SerializedName("vote_average") val voteAverage: Float?,
)

@Suppress("CyclomaticComplexMethod")
fun MovieDetailsDataResponse.toMovie(): Movie {
    val input = this
    return Movie(
        movieId = input.movieId ?: 0,
        backdropPath = if (input.backdropPath != null) {
            "$BASE_IMAGES_PATH/original/${input.backdropPath}"
        } else {
            ""
        },
        isForAdult = input.isForAdult ?: false,
        budget = input.budget ?: 0,
        genres = input.genres?.map { itemGenre ->
            Genre(
                genreId = itemGenre.genreId ?: 0,
                name = itemGenre.name ?: "",
            )
        }.orEmpty(),
        homepageUrl = input.homepageUrl ?: "",
        posterPath = if (input.posterPath != null) {
            "$BASE_IMAGES_PATH/w300/${input.posterPath}"
        } else {
            ""
        },
        language = input.language ?: "",
        originalTitle = input.originalTitle ?: "",
        title = input.title ?: "",
        overview = input.overview ?: "",
        releaseDate = input.releaseDate ?: Date(),
        voteAverage = input.voteAverage ?: 0f,
    )
}