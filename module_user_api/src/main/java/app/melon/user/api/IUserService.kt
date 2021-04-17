package app.melon.user.api

import android.content.Context
import androidx.fragment.app.Fragment

interface IUserService {
    fun navigateToUserProfile(context: Context, uid: String)

    fun navigateToFollowersList(context: Context, uid: String)

    fun navigateToFollowingList(context: Context, uid: String)

    fun navigateToMyProfile(context: Context)

    fun buildUserListFragment(config: UserListConfig): Fragment
}