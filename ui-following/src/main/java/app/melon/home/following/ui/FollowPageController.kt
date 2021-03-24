package app.melon.home.following.ui

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.resultentities.FollowingEntryWithFeed
import app.melon.feed.FeedControllerDelegate
import com.airbnb.epoxy.EpoxyModel


class FollowPageController(
    context: Context
) : BasePagingController<FollowingEntryWithFeed>(context) {

    private val delegate = FeedControllerDelegate(context)

    override fun buildItemModel(currentPosition: Int, item: FollowingEntryWithFeed?): EpoxyModel<*> =
        delegate.buildFeedItem(
            feedProvider = { item!!.feed },
            idProvider = { "following_feed_${item!!.entry.feedId}" }
        )
}