package app.melon.account

import app.melon.account.data.AccountApi
import app.melon.account.data.AccountResponse
import app.melon.account.data.remote.UserDetailResponse
import app.melon.base.network.MelonApiService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class AccountApiService @Inject constructor(
    private val api: AccountApi
) : MelonApiService() {

    suspend fun register(
        username: String,
        password: String
    ): Result<AccountResponse> {
        return apiCall {
            api.register(username, password)
        }
    }

    suspend fun login(
        username: String,
        password: String
    ): Result<AccountResponse> {
        return apiCall {
            api.login(username, password)
        }
    }

    suspend fun logout(): Result<AccountResponse> {
        return apiCall {
            api.logout()
        }
    }

    suspend fun fetchUserDetail(): Result<UserDetailResponse> {
        return call {
            api.fetchUserDetail()
        }
    }
}