package app.melon.account

import app.melon.account.api.UserManager
import javax.inject.Inject
import kotlin.random.Random


/**
 * UserDataRepository contains user-specific data such as username and unread notifications.
 */
@LoggerUserScope
class UserDataRepository @Inject constructor(
    private val userManager: UserManager
) {

    var unreadNotifications: Int

    init {
        unreadNotifications = randomInt()
    }

    fun refreshUnreadNotifications() {
        unreadNotifications = randomInt()
    }
}

fun randomInt(): Int {
    return Random.nextInt(until = 100)
}
