package pro.jsandoval.movieapp.utils.exception

open class CustomHttpError @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null,
    open val httpCode: Int? = null,
) : Exception(message, cause) {

    override fun toString(): String {
        return """
            Message = "$message",
            HTTP code = "$httpCode",
            Cause = "$cause"
        """.trimIndent()
    }
}
