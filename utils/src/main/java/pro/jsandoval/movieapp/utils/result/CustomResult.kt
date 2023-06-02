package pro.jsandoval.movieapp.utils.result

import pro.jsandoval.movieapp.utils.ext.toKnownException

@Suppress("TooGenericExceptionCaught")
sealed class CustomResult<out T> {
    data class Success<T>(val data: T) : CustomResult<T>()
    data class Error(val error: Throwable? = null) : CustomResult<Nothing>()

    fun <T, R> Success<T>.map(mapper: DataToDomainMapper<T, R>): CustomResult<R> {
        return try {
            val domainData = mapper.mapToDomain(data)
            Success(domainData)
        } catch (exc: Exception) {
            Error(exc.toKnownException())
        }
    }
}

fun <T> CustomResult<T>.onSuccess(block: (data: T) -> Unit): CustomResult<T> {
    if (this is CustomResult.Success) {
        block.invoke(data)
    }
    return this
}

fun <T> CustomResult<T>.onError(block: (error: Throwable?) -> Unit): CustomResult<T> {
    if (this is CustomResult.Error) {
        block.invoke(error)
    }
    return this
}
