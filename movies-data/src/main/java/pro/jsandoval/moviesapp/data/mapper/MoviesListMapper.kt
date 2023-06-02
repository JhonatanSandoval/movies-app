package pro.jsandoval.moviesapp.data.mapper

import pro.jsandoval.movieapp.utils.exception.InvalidResponseException
import pro.jsandoval.movieapp.utils.result.DataToDomainMapper
import pro.jsandoval.moviesapp.data.remote.response.GetMoviesResponse
import pro.jsandoval.moviesapp.data.remote.response.toMovie
import pro.jsandoval.moviesapp.model.Movie
import javax.inject.Inject

class MoviesListMapper @Inject constructor() : DataToDomainMapper<GetMoviesResponse, List<Movie>> {

    @Suppress("TooGenericExceptionCaught", "SwallowedException", "CyclomaticComplexMethod")
    override fun mapToDomain(input: GetMoviesResponse): List<Movie> {
        return try {
            input.results?.map { item -> item.toMovie() }.orEmpty()
        } catch (exc: Exception) {
            throw InvalidResponseException("There was an error parsing data from response | ${exc.message}")
        }
    }
}