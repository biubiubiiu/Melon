package app.melon.home.following.ui

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.resultentities.FollowingEntryWithFeed
import app.melon.feed.FeedControllerDelegate
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class FollowPageController @AssistedInject constructor(
    @Assisted context: Context,
    private val factory: FeedControllerDelegate.Factory
) : BasePagingController<FollowingEntryWithFeed>(context) {

    private val delegate by lazy {
        factory.create(context)
    }

    override fun buildItemModel(currentPosition: Int, item: FollowingEntryWithFeed?): EpoxyModel<*> =
        delegate.buildFeedItem(
            feedProvider = { item!!.feed },
            idProvider = { "following_feed_${item!!.entry.feedId}" }
        )

    @AssistedFactory
    interface Factory {
        fun create(context: Context): FollowPageController
    }
}