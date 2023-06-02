package pro.jsandoval.moviesapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pro.jsandoval.moviesapp.model.Movie
import java.util.Date

const val MOVIES_TABLE_NAME = "movies"

@Entity(tableName = MOVIES_TABLE_NAME)
data class MovieEntity(
    // just saving the needed values for the list
    @PrimaryKey(autoGenerate = false) val movieId: Long,
    @ColumnInfo var posterPath: String,
    @ColumnInfo var title: String,
    @ColumnInfo var releaseDate: Date,
)

fun Movie.toEntity(): MovieEntity {
    val input = this
    return MovieEntity(
        movieId = input.movieId,
        posterPath = input.posterPath,
        title = input.title,
        releaseDate = input.releaseDate,
    )
}