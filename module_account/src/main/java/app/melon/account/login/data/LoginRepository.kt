package app.melon.account.login.data

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
class LoginRepository @Inject constructor(
    private val dataSource: LoginDataSource
) {

    suspend fun logout() {
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<String> {
        // handle login
        return withContext(Dispatchers.IO) {
            dataSource.login(username, password)
        }
    }
}