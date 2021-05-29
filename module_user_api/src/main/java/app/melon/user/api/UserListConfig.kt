package app.melon.user.api

import app.melon.data.constants.FOLLOWERS_USER
import app.melon.data.constants.FOLLOWING_USER
import app.melon.data.constants.NEARBY_USER
import app.melon.data.constants.UserPageType
import java.io.Serializable


sealed class UserListConfig : Serializable {
    @UserPageType abstract val pageType: Int
}

object NearbyUserList: UserListConfig() {
    override val pageType: Int = NEARBY_USER
}

class FollowingUserList(
    val uid: String
) : UserListConfig() {
    override val pageType: Int = FOLLOWING_USER
}

class FollowerUserList(
    val uid: String
) : UserListConfig() {
    override val pageType: Int = FOLLOWERS_USER
}