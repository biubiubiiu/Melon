package app.melon.user.ui.detail

import android.view.View
import app.melon.base.framework.SingleEvent
import app.melon.data.entities.User
import app.melon.data.resultentities.FeedAndAuthor

data class UserProfileViewState(
    val uid: String = "",
    val user: User? = null,
    val pageItems: List<FeedAndAuthor>? = null,
    val enterMorePosts: SingleEvent<View>? = null,
    val refreshing: Boolean = false,
    val error: Throwable? = null
)