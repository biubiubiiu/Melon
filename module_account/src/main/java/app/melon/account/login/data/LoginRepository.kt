package app.melon.account.login.data

import androidx.annotation.WorkerThread
import app.melon.account.AccountApiService
import app.melon.account.data.LoginStatus
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
@Singleton
internal class LoginRepository @Inject constructor(
    private val accountApiService: AccountApiService
) {

    @WorkerThread
    internal suspend fun logout() {
        // TODO: revoke authentication
//        return accountApiService.logout().fold(
//            onSuccess = {
//                Result.success(Any())
//            },
//            onFailure = {
//                Result.failure(it)
//            }
//        )
    }

    @WorkerThread
    internal suspend fun login(username: String, password: String): LoginStatus<String> {
        return runCatching {
            val response = accountApiService.login(username, password).getOrThrow()
            response
        }.fold(
            onSuccess = { response ->
                if (response.code == 200) {
                    LoginStatus.Success(response.result!!.token!!)
                } else {
                    LoginStatus.Failure
                }
            }, onFailure = { throwable ->
                LoginStatus.Error.Generic(throwable)
            }
        )
    }

    internal suspend fun signUp(username: String, password: String): Result<Boolean> {
        return runCatching {
            accountApiService.register(username, password).getOrThrow()
        }.fold(
            onSuccess = {
                Result.success(true)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}