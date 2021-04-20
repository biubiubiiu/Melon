package app.melon.user.ui.controller

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.entities.User
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class UserPageController @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted private val idProvider: (User?, Int) -> String,
    @Assisted private val showFollowButton: Boolean,
    factory: UserControllerDelegate.Factory
) : BasePagingController<User>(
    context,
    sameContentIndicator = { oldItem, newItem -> oldItem.id == newItem.id }
) {

    private val delegate = factory.create(context)

    override fun buildItemModel(currentPosition: Int, item: User?): EpoxyModel<*> {
        return delegate.buildUserItem(
            dataProvider = { item!! },
            idProvider = { idProvider(item, currentPosition) },
            showFollowButton = showFollowButton
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(
            context: Context,
            idProvider: (User?, Int) -> String,
            showFollowButton: Boolean
        ): UserPageController
    }
}