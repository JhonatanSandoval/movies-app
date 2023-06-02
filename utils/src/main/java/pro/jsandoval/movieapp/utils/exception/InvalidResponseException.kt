package pro.jsandoval.movieapp.utils.exception

open class InvalidResponseException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null,
    open val httpCode: Int? = null,
    val rawRequest: String? = null,
    val rawResponse: String? = null,
) : Exception(message, cause) {

    override fun toString(): String {
        return """
            Message = "$message",
            HTTP code = "$httpCode",
            RAW Request = "$rawRequest",
            RAW Response = "$rawResponse"
            Cause = "$cause"
        """.trimIndent()
    }
}
