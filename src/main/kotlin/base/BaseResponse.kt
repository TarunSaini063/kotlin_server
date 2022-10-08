package base

import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class BaseResponse<T>(
    @SerialName("statusCode") val status: Int
) {
    @Serializable
    data class Success<T>(
        val data: T, val message: String? = null, @Transient val statusCode: HttpStatusCode = HttpStatusCode.OK
    ) : BaseResponse<T>(statusCode.value)

    @Serializable
    data class Error<T>(
        val data: T? = null, val message: String? = null, @Transient val statusCode: HttpStatusCode = HttpStatusCode.OK
    ) : BaseResponse<T>(statusCode.value)

    val httpStatusCode
        get() = HttpStatusCode.fromValue(status)
}

@Serializable
object DefaultResponse

@Serializable
data class DefaultError(
    val message: String? = "Something went wrong..."
)