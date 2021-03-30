package app.melon.account.login.data

import javax.inject.Inject
import app.melon.util.base.Result

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository @Inject constructor(
    private val dataSource: LoginDataSource
) {

    suspend fun logout() {
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<String> {
        // handle login
        return dataSource.login(username, password)
    }
}