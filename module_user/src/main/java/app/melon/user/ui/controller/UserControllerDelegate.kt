package app.melon.user.ui.controller

import android.content.Context
import app.melon.data.entities.User
import app.melon.user.UserActions
import app.melon.user.api.IUserService
import app.melon.user.ui.widget.UserItem_
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class UserControllerDelegate @AssistedInject constructor(
    @Assisted private val context: Context,
    private val userService: IUserService
) : UserActions {

    fun buildUserItem(
        dataProvider: () -> User,
        idProvider: () -> String = { "user_${dataProvider.invoke().id}" },
        showFollowButton: Boolean // TODO unused now
    ): EpoxyModel<*> {
        val item = dataProvider.invoke()
        return UserItem_()
            .id(idProvider.invoke())
            .user(item)
            .holderClickListener { this.onHolderClick(it) }
    }

    override fun onHolderClick(user: User) {
        userService.navigateToUserProfile(context, user.id)
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context): UserControllerDelegate
    }
}