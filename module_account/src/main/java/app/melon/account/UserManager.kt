package app.melon.account

import app.melon.account.api.UserManager
import app.melon.account.di.UserComponent
import app.melon.account.login.data.LoginRepository
import app.melon.util.storage.Storage
import app.melon.data.entities.User
import app.melon.util.base.Success
import app.melon.util.network.TokenManager
import app.melon.util.storage.RegistrationStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Handles User lifecycle. Manages registrations, logs in and logs out.
 * Knows when the user is logged in.
 */
@Singleton
class UserManagerImpl @Inject constructor(
    @RegistrationStorage private val storage: Storage,
    private val userComponentFactory: UserComponent.Factory,
    private val repo: LoginRepository,
    private val tokenManager: TokenManager
) : UserManager() {

    companion object {
        private const val REGISTERED_USER = "registered_user"
        private const val PASSWORD_SUFFIX = "password"
    }

    var userComponent: UserComponent? = null
        private set

    override val savedUsername: String get() = storage.getString(REGISTERED_USER)
    override val savePassword: String get() = storage.getString("$savedUsername$PASSWORD_SUFFIX")

    // in-memory cache of the loggedInUser object
    override val user: Flow<User?> = emptyFlow()

    override fun isUserLoggedIn() = userComponent != null

    override fun isUserRegistered() = storage.getString(REGISTERED_USER).isNotEmpty()

    override fun registerUser(username: String, password: String) {
        storage.setString(REGISTERED_USER, username)
        storage.setString("$username$PASSWORD_SUFFIX", password)
        userJustLoggedIn()
    }

    override suspend fun loginUser(): Boolean {
        return loginUser(this.savedUsername, this.savePassword)
    }

    override suspend fun loginUser(username: String, password: String): Boolean {
        val result = repo.login(username, password)
        return if (result is Success) {
            tokenManager.updateToken(token = result.get())
            userJustLoggedIn()
            true
        } else {
            false
        }
    }

    override fun logout() {
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
