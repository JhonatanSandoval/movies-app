package pro.jsandoval.moviesapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import pro.jsandoval.movieapp.utils.result.CustomResult
import pro.jsandoval.moviesapp.data.local.dao.MoviesDao
import pro.jsandoval.moviesapp.data.local.entity.toEntity
import pro.jsandoval.moviesapp.data.mapper.MovieDetailsMapper
import pro.jsandoval.moviesapp.data.mapper.MoviesEntityListMapper
import pro.jsandoval.moviesapp.data.mapper.MoviesListMapper
import pro.jsandoval.moviesapp.data.remote.MoviesApi
import pro.jsandoval.moviesapp.model.Movie
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

interface MoviesRepository {
    fun getPopularMovies(): Flow<CustomResult<List<Movie>>>
    fun getFavoritesMovies(): Flow<List<Movie>>
    fun getMoviesDetails(movieId: Long): Flow<CustomResult<Movie>>
    suspend fun isMovieSavedAsFavorite(movieId: Long): Boolean
    suspend fun saveMovieLocally(movie: Movie)
    suspend fun deleteMovie(movieId: Long)
}

@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    private val moviesDao: MoviesDao,
    private val moviesListMapper: MoviesListMapper,
    private val moviesEntityListMapper: MoviesEntityListMapper,
    private val movieDetailsMapper: MovieDetailsMapper,
) : BaseRepository(), MoviesRepository {

    override fun getPopularMovies(): Flow<CustomResult<List<Movie>>> {
        return flow {
            val apiCall = safeApiCall { moviesApi.getPopularMovies() }
            emit(apiCall.mapOrDefineError(moviesListMapper))
        }.catch { error -> Timber.e(error) }
    }

    override suspend fun isMovieSavedAsFavorite(movieId: Long): Boolean {
        val movieEntity = moviesDao.findById(movieId)
        return movieEntity != null && movieEntity.movieId == movieId
    }

    override suspend fun saveMovieLocally(movie: Movie) {
        val entity = movie.toEntity()
        moviesDao.insert(entity)
    }

    override suspend fun deleteMovie(movieId: Long) {
        moviesDao.deleteById(movieId)
    }

    override fun getFavoritesMovies(): Flow<List<Movie>> {
        return moviesDao.getFavorites().map { moviesEntityListMapper.mapToDomain(it) }
    }

    override fun getMoviesDetails(movieId: Long): Flow<CustomResult<Movie>> {
        return flow {
            when (val apiCall = safeApiCall { moviesApi.getMovieDetails(movieId) }) {
                is CustomResult.Success -> {
                    // check if the movie was saved locally (is favorite)
                    val movie = movieDetailsMapper.mapToDomain(apiCall.data)
                    movie.isFavorite = isMovieSavedAsFavorite(movie.movieId)
                    emit(CustomResult.Success(movie))
                }

                is CustomResult.Error -> {
                    emit(apiCall)
                }
            }
        }.catch { error -> Timber.e(error) }
    }
}