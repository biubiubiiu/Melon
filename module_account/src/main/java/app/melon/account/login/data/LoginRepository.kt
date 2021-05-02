package app.melon.account.login.data

import app.melon.account.signup.data.SignUpDataSource
import javax.inject.Inject
import app.melon.util.base.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Singleton

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
@Singleton
internal class LoginRepository @Inject constructor(
    private val loginDataSource: LoginDataSource,
    private val signUpDataSource: SignUpDataSource
) {

    internal suspend fun logout() {
        loginDataSource.logout()
    }

    internal suspend fun login(username: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            loginDataSource.login(username, password)
        }
    }

    internal suspend fun signUp(username: String, password: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            signUpDataSource.signUp(
                username = username,
                password = password
            )
        }
    }
}