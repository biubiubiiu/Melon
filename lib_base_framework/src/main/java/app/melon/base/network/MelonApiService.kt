package app.melon.base.network

import androidx.annotation.CallSuper
import app.melon.data.dto.BaseApiResponse
import app.melon.util.extensions.toException
import retrofit2.Response

open class MelonApiService : BaseService() {

    override fun parseCustomError(
        responseMessage: String,
        responseCode: Int,
        errorBodyJson: String
    ): ApiBaseException {
        return mapApiThrowable(responseMessage, responseCode)
    }

    suspend fun <T : Any> call(call: suspend () -> Response<BaseApiResponse<T>>): Result<T> {
        val result = super.apiCall(call)
        return when {
            result.isFailure -> Result.failure(result.exceptionOrNull()!!)
            else -> {
                val responseBody: BaseApiResponse<T> = result.getOrNull()!!
                if (!responseBody.isSuccess) {
                    Result.failure(responseBody.errorMessage.toException())
                } else if (responseBody.data == null) {
                    Result.failure(NullBodyException("response body data can't be null"))
                } else {
                    Result.success(responseBody.data!!)
                }
            }
        }
    }

    @CallSuper
    protected open fun mapApiThrowable(message: String, code: Int): ApiBaseException = ApiBaseException()
}