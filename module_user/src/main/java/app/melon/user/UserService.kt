package app.melon.user

import android.content.Context
import androidx.fragment.app.Fragment
import app.melon.user.api.IUserService
import app.melon.user.api.UserListConfig
import app.melon.user.ui.CommonUserFragment
import javax.inject.Singleton

@Singleton
class UserService : IUserService {

    override fun navigateToUserProfile(context: Context, uid: String) {
        ProfileActivity.start(context, uid)
    }

    override fun navigateToFollowersList(context: Context, uid: String) {
        FollowersActivity.start(context, uid)
    }

    override fun navigateToFollowingList(context: Context, uid: String) {
        FollowingActivity.start(context, uid)
    }

    override fun navigateToMyProfile(context: Context) {
        ProfileActivity.start(context)
    }

    override fun buildUserListFragment(config: UserListConfig): Fragment {
        return CommonUserFragment.newInstance(config)
    }
}