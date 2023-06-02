package pro.jsandoval.moviesapp.data.remote

import pro.jsandoval.moviesapp.data.remote.response.GetMoviesResponse
import pro.jsandoval.moviesapp.data.remote.response.MovieDetailsDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesApi {

    @GET("popular")
    suspend fun getPopularMovies(): Response<GetMoviesResponse>

    @GET("{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Long,
    ): Response<MovieDetailsDataResponse>
}