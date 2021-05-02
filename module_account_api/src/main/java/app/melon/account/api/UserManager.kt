package app.melon.account.api

import app.melon.data.entities.User
import kotlinx.coroutines.flow.Flow

abstract class UserManager {

    // in-memory cache of the loggedInUser object
    abstract val user: User?
    abstract fun observeUser(): Flow<User?>
}