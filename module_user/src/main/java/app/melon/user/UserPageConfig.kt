package app.melon.user

import app.melon.data.constants.NEARBY_USER
import app.melon.data.constants.UserPageType

data class UserPageConfig(
    @UserPageType val pageType: Int,
    val idPrefix: String, // Used as epoxy model id prefix
    val showFollowButton: Boolean = pageType != NEARBY_USER
)