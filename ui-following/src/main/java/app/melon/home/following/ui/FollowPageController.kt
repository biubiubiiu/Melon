package app.melon.home.following.ui

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.resultentities.FollowingEntryWithFeed
import app.melon.feed.FeedControllerDelegate
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject


class FollowPageController @AssistedInject constructor(
    @Assisted context: Context
) : BasePagingController<FollowingEntryWithFeed>(context) {

    @Inject internal lateinit var factory: FeedControllerDelegate.Factory

    private val delegate get() = factory.create(context)

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