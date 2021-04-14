package app.melon.home.following.ui

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.resultentities.EntryWithFeedAndAuthor
import app.melon.feed.FeedControllerDelegate
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class FollowPageController @AssistedInject constructor(
    @Assisted context: Context,
    feedControllerFactory: FeedControllerDelegate.Factory
) : BasePagingController<EntryWithFeedAndAuthor>(context) {

    private val feedDelegate = feedControllerFactory.create(context)

    override fun buildItemModel(currentPosition: Int, item: EntryWithFeedAndAuthor?) =
        feedDelegate.buildFeedItem(
            dataProvider = { item!!.compoundItem },
            idProvider = { "following_feed_$currentPosition" }
        )

    @AssistedFactory
    interface Factory {
        fun create(context: Context): FollowPageController
    }
}