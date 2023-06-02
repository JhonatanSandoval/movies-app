package pro.jsandoval.movieapp.utils.ext

import android.accounts.NetworkErrorException
import android.util.MalformedJsonException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pro.jsandoval.movieapp.utils.exception.CustomHttpError
import pro.jsandoval.movieapp.utils.exception.InvalidResponseException
import pro.jsandoval.moviesapp.model.TmdbError
import retrofit2.HttpException
import java.io.IOException

private const val MINIMUM_HTTP_CODE_TO_HANDLE = 400

fun Throwable.toKnownException(): Throwable {
    return when (this) {
        is MalformedJsonException -> InvalidResponseException(cause = this)
        is IOException -> NetworkErrorException(this)
        is HttpException -> {
            if (code() >= MINIMUM_HTTP_CODE_TO_HANDLE) {
                toKnownHttpException()
            } else this
        }

        else -> this
    }
}

@Suppress("SwallowedException", "TooGenericExceptionCaught")
private fun HttpException.toKnownHttpException(): Throwable {
    return try {
        val type = object : TypeToken<TmdbError>() {}.type
        val tmdbError = Gson().fromJson<TmdbError>(response()?.errorBody()!!.charStream(), type)
        CustomHttpError(message = tmdbError.status_message, cause = this, httpCode = code())
    } catch (exc: Exception) {
        CustomHttpError(cause = this)
    }
}
