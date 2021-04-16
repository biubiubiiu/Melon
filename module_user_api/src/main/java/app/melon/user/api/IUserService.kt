package app.melon.user.api

import android.content.Context

interface IUserService {
    fun navigateToUserProfile(context: Context, uid: String)

    fun navigateToFollowersList(context: Context, uid: String)

    fun navigateToFollowingList(context: Context, uid: String)

    fun navigateToMyProfile(context: Context)
}