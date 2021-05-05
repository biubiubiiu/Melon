package app.melon.util.timesync

import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import app.melon.util.base.Success
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toException
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TimeSyncer @Inject constructor(
    private val service: TimeService
) {

    // TODO fail fast when network is not available
    suspend fun getServerTime(): Result<String> {
        return withTimeoutOrNull(5000L) {
            try {
                val apiResponse = withContext(Dispatchers.IO) {
                    service.serverTime()
                        .executeWithRetry()
                        .toResult()
                        .getOrThrow()
                }
                if (!apiResponse.isSuccess) {
                    ErrorResult<String>(apiResponse.errorMessage.toException())
                } else {
                    Success(apiResponse.data!!.time)
                }
            } catch (e: Exception) {
                ErrorResult<String>(e)
            }
        } ?: ErrorResult("Server time sync time out".toException())
    }
}