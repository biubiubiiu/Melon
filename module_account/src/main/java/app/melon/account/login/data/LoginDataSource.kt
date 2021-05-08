package app.melon.account.login.data

import androidx.annotation.WorkerThread
import app.melon.account.AccountApiService
import kotlin.runCatching
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
internal class LoginDataSource @Inject constructor(
    private val accountApiService: AccountApiService
) {

    @WorkerThread
    suspend fun login(username: String, password: String): Result<String> {
        return Result.success("Fake Token") // TODO remove this line
        return runCatching {
            val response = accountApiService.login(username, password).getOrThrow()
            response.result!!.token!!
        }.fold(
            onSuccess = {
                Result.success(it)
            }, onFailure = {
                Result.failure(it)
            }
        )
    }

    @WorkerThread
    suspend fun logout(): Result<Any> {
        // TODO: revoke authentication
        return Result.success(Any()) // TODO remove this line
        return accountApiService.logout().fold(
            onSuccess = {
                Result.success(Any())
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}