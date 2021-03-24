package app.melon.user

import app.melon.data.entities.Feed
import app.melon.data.entities.User

data class UserProfileViewState(
    val uid: String = "",
    val user: User? = null,
    val feeds: List<Feed>? = null,
    val refreshing: Boolean = false
)