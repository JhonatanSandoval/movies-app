package pro.jsandoval.moviesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pro.jsandoval.moviesapp.data.local.dao.MoviesDao
import pro.jsandoval.moviesapp.data.local.entity.MovieEntity

private const val DB_VERSION = 1
const val DB_NAME = "MoviesApp-DB"

@Database(
    entities = [
        MovieEntity::class,
    ],
    version = DB_VERSION,
    exportSchema = false,
)
@TypeConverters(MoviesAppConverters::class)
abstract class MoviesAppDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
}