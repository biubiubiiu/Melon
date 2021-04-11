package app.melon.account

import app.melon.account.di.RegistrationStorage
import app.melon.account.di.UserComponent
import app.melon.account.login.data.LoginRepository
import app.melon.account.storage.Storage
import app.melon.data.entities.User
import app.melon.util.base.Success
import app.melon.util.network.TokenManager
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Handles User lifecycle. Manages registrations, logs in and logs out.
 * Knows when the user is logged in.
 */
@Singleton
class UserManager @Inject constructor(
    @RegistrationStorage private val storage: Storage,
    private val userComponentFactory: UserComponent.Factory,
    private val repo: LoginRepository,
    private val tokenManager: TokenManager
) {

    companion object {
        private const val REGISTERED_USER = "registered_user"
        private const val PASSWORD_SUFFIX = "password"
    }

    var userComponent: UserComponent? = null
        private set

    private val username: String get() = storage.getString(REGISTERED_USER)
    private val password: String get() = storage.getString("$username$PASSWORD_SUFFIX")

    // in-memory cache of the loggedInUser object
    var user: User? = null
        private set

    fun isUserLoggedIn() = userComponent != null

    fun isUserRegistered() = storage.getString(REGISTERED_USER).isNotEmpty()

    fun registerUser(username: String, password: String) {
        storage.setString(REGISTERED_USER, username)
        storage.setString("$username$PASSWORD_SUFFIX", password)
        userJustLoggedIn()
    }

    suspend fun loginUser(): Boolean {
        return loginUser(this.username, this.password)
    }

    suspend fun loginUser(username: String, password: String): Boolean {
        val result = repo.login(username, password)
        return if (result is Success) {
            tokenManager.updateToken(token = result.get())
            userJustLoggedIn()
            true
        } else {
            false
        }
    }

    fun logout() {
        tokenManager.clearToken()
        userComponent = null
    }

    fun unregister() {
        val username = storage.getString(REGISTERED_USER)
        storage.setString(REGISTERED_USER, "")
        storage.setString("$username$PASSWORD_SUFFIX", "")
        logout()
    }

    private fun userJustLoggedIn() {
        userComponent = userComponentFactory.create()
    }
}
