package app.melon.user

import android.content.Context
import app.melon.user.api.IUserService

class UserService : IUserService {

    override fun navigateToUserProfile(context: Context, uid: String) {
        // TODO add business logic here
        UserProfileActivity.start(context, uid)
    }
}