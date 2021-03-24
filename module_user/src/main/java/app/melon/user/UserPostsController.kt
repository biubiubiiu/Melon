package app.melon.user

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.entities.Feed
import app.melon.feed.FeedControllerDelegate
import com.airbnb.epoxy.EpoxyModel

class UserPostsController(context: Context) : BasePagingController<Feed>(context) {

    private val delegate = FeedControllerDelegate(context)

    override fun buildItemModel(currentPosition: Int, item: Feed?): EpoxyModel<*> =
        delegate.buildFeedItem(
            feedProvider = { item!! },
            idProvider = { "user_posts_$currentPosition" }
        )
}