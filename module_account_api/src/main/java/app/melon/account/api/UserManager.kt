package app.melon.account.api

import app.melon.data.entities.User
import kotlinx.coroutines.flow.Flow

abstract class UserManager {

    protected abstract val savedUsername: String
    protected abstract val savePassword: String

    // in-memory cache of the loggedInUser object
    abstract val user: User?
    abstract fun observeUser(): Flow<User?>

    abstract fun isUserLoggedIn(): Boolean

    abstract fun isUserRegistered(): Boolean

    abstract fun registerUser(username: String, password: String)

    abstract suspend fun loginUser(): Boolean

    abstract suspend fun loginUser(username: String, password: String): Boolean

    abstract fun logout()
}