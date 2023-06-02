package pro.jsandoval.moviesapp.data.repository

import pro.jsandoval.movieapp.utils.ext.toKnownException
import pro.jsandoval.movieapp.utils.result.CustomResult
import pro.jsandoval.movieapp.utils.result.DataToDomainMapper
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

@Suppress("UnnecessaryAbstractClass", "ReturnCount")
abstract class BaseRepository {

    @Suppress("TooGenericExceptionCaught")
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): CustomResult<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return CustomResult.Success(body)
                }
            }
            throw HttpException(response)
        } catch (exc: Exception) {
            Timber.e(exc, "There was an error making the api call.")
            return CustomResult.Error(exc.toKnownException())
        }
    }

    fun <T, R> CustomResult<T>.mapOrDefineError(
        mapper: DataToDomainMapper<T, R>,
    ): CustomResult<R> {
        return when (this) {
            is CustomResult.Success -> map(mapper)
            is CustomResult.Error -> this
        }
    }
}
