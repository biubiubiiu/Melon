package app.melon.base.network

import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.Result

abstract class BaseService {

    protected abstract fun parseCustomError(
        responseMessage: String,
        responseCode: Int,
        errorBodyJson: String
    ): ApiBaseException

    protected suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Result<T> {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (t: Throwable) {
            return Result.failure(mapNetworkThrowable(t))
        }

        return if (!response.isSuccessful) {
            val errorBody = response.errorBody()
            @Suppress("BlockingMethodInNonBlockingContext")
            Result.failure(parseCustomError(response.message(), response.code(), errorBody?.string() ?: ""))
        } else {
            return if (response.body() == null) {
                Result.failure(NullBodyException("response.body() can't be null"))
            } else {
                Result.success(response.body()!!)
            }
        }
    }

    // TODO: complete logic
    private fun mapNetworkThrowable(throwable: Throwable): NetworkBaseException {
        return when (throwable) {
            is HttpException -> NetworkBaseException(throwable.message.orEmpty())
            is SocketTimeoutException -> NetworkBaseException(throwable.message.orEmpty())
            else -> NetworkBaseException()
        }
    }
}