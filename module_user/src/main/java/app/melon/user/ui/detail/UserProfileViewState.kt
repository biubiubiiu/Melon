package app.melon.user.ui.detail

import app.melon.data.entities.User
import app.melon.data.resultentities.FeedAndAuthor

data class UserProfileViewState(
    val uid: String = "",
    val user: User? = null,
    val pageItems: List<FeedAndAuthor>? = null,
    val refreshing: Boolean = false,
    val error: Throwable? = null
)