package app.melon.user.ui.posts

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.entities.Feed
import app.melon.feed.FeedControllerDelegate
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

class UserPostsController @AssistedInject constructor(
    @Assisted context: Context
) : BasePagingController<Feed>(context) {

    @Inject internal lateinit var factory: FeedControllerDelegate.Factory

    private val delegate get() = factory.create(context)

    override fun buildItemModel(currentPosition: Int, item: Feed?): EpoxyModel<*> =
        delegate.buildFeedItem(
            feedProvider = { item!! },
            idProvider = { "user_posts_$currentPosition" }
        )

    @AssistedFactory
    interface Factory {
        fun create(context: Context): UserPostsController
    }
}