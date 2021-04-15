package app.melon.user.ui.posts

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.ui.controller.FeedControllerDelegate
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class UserPostsController @AssistedInject constructor(
    @Assisted context: Context,
    private val factory: FeedControllerDelegate.Factory
) : BasePagingController<FeedAndAuthor>(context) {

    private val delegate by lazy {
        factory.create(context)
    }

    override fun buildItemModel(currentPosition: Int, item: FeedAndAuthor?)  =
        delegate.buildFeedItem(
            dataProvider = { item!! },
            idProvider = { "user_posts_$currentPosition" }
        )

    @AssistedFactory
    interface Factory {
        fun create(context: Context): UserPostsController
    }
}