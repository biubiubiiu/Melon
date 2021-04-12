package app.melon.user

import android.content.Context
import app.melon.user.api.IUserService
import javax.inject.Singleton

@Singleton
class UserService : IUserService {

    override fun navigateToUserProfile(context: Context, uid: String) {
        // TODO add business logic here
        UserProfileActivity.start(context, uid)
    }

    override fun navigateToMyProfile(context: Context) {
        ProfileActivity.start(context)
    }
}