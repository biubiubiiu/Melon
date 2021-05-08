package app.melon.account.signup.data

import androidx.annotation.WorkerThread
import app.melon.account.AccountApiService
import app.melon.util.encrypt.EncryptUtils
import javax.inject.Inject
import kotlin.runCatching


internal class SignUpDataSource @Inject constructor(
    private val accountApiService: AccountApiService
) {

    @WorkerThread
    internal suspend fun signUp(username: String, password: String): Result<Boolean> {
        return Result.success(true) // TODO remove this line
        return runCatching {
            val response = accountApiService.register(username, password).getOrThrow()
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