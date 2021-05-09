package app.melon.util.timesync

import app.melon.util.extensions.fetchBodyWithRetry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TimeSyncer @Inject constructor(
    private val service: TimeService
) {

    // TODO fail fast when network is not available
    suspend fun getServerTime(): Result<String> {
        return runCatching {
            withTimeout(5000L) {
                val response = withContext(Dispatchers.IO) {
                    service.serverTime().fetchBodyWithRetry()
                }
                response.data!!.time
            }
        }.fold(
            onSuccess = {
                Result.success(it)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}