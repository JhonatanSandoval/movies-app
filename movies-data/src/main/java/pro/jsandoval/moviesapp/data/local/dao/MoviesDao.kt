package pro.jsandoval.moviesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pro.jsandoval.moviesapp.data.local.entity.MOVIES_TABLE_NAME
import pro.jsandoval.moviesapp.data.local.entity.MovieEntity

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MovieEntity)

    @Query(" SELECT * FROM $MOVIES_TABLE_NAME ")
    fun getFavorites(): Flow<List<MovieEntity>>

    @Query(" SELECT * FROM $MOVIES_TABLE_NAME WHERE movieId = :movieId ")
    suspend fun findById(movieId: Long): MovieEntity?

    @Query(" DELETE FROM $MOVIES_TABLE_NAME WHERE movieId = :movieId ")
    suspend fun deleteById(movieId: Long)
}