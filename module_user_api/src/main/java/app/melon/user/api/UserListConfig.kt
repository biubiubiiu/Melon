package app.melon.user.api

import androidx.annotation.FloatRange
import app.melon.data.constants.FOLLOWERS_USER
import app.melon.data.constants.FOLLOWING_USER
import app.melon.data.constants.NEARBY_USER
import app.melon.data.constants.UserPageType
import java.io.Serializable

sealed class UserListConfig : Serializable {
    abstract val listItemIdPrefix: String
    abstract val refreshInterceptor: (() -> Boolean)?
    @UserPageType abstract val pageType: Int
}

class NearbyUserList(
    override val listItemIdPrefix: String,
    @FloatRange(from = -180.0, to = 180.0) val longitude: Double,
    @FloatRange(from = -90.0, to = 90.0) val latitude: Double,
    override val refreshInterceptor: (() -> Boolean)? = null
) : UserListConfig() {
    override val pageType: Int = NEARBY_USER
}

class FollowingUserList(
    override val listItemIdPrefix: String,
    val uid: String,
    override val refreshInterceptor: (() -> Boolean)? = null
) : UserListConfig() {
    override val pageType: Int = FOLLOWING_USER
}

class FollowerUserList(
    override val listItemIdPrefix: String,
    val uid: String,
    override val refreshInterceptor: (() -> Boolean)? = null
) : UserListConfig() {
    override val pageType: Int = FOLLOWERS_USER
}