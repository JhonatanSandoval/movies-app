package pro.jsandoval.moviesapp.data.mapper

import pro.jsandoval.movieapp.utils.exception.InvalidResponseException
import pro.jsandoval.movieapp.utils.result.DataToDomainMapper
import pro.jsandoval.moviesapp.data.remote.response.MovieDetailsDataResponse
import pro.jsandoval.moviesapp.data.remote.response.toMovie
import pro.jsandoval.moviesapp.model.Movie
import javax.inject.Inject

class MovieDetailsMapper @Inject constructor() : DataToDomainMapper<MovieDetailsDataResponse, Movie> {

    @Suppress("SwallowedException", "TooGenericExceptionCaught")
    override fun mapToDomain(input: MovieDetailsDataResponse): Movie {
        return try {
            input.toMovie()
        } catch (exc: Exception) {
            throw InvalidResponseException("There was an error parsing data from response | ${exc.message}")
        }
    }
}