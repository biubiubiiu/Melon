package app.melon.home.recommend.ui

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.resultentities.RecommendedEntryWithFeed
import app.melon.feed.FeedControllerDelegate
import app.melon.group.GroupRenderer
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope


class RecommendPageController @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted private val scope: CoroutineScope,
    feedControllerFactory: FeedControllerDelegate.Factory,
    groupRendererFactory: GroupRenderer.Factory
) : BasePagingController<RecommendedEntryWithFeed>(context) {

    private val feedDelegate = feedControllerFactory.create(context)
    private val groupDelegate = groupRendererFactory.create(context, scope)

    override fun buildItemModel(currentPosition: Int, item: RecommendedEntryWithFeed?): EpoxyModel<*> =
        feedDelegate.buildFeedItem(
            feedProvider = { item!!.feed },
            idProvider = { "recommend_feed${item!!.entry.feedId}" }
        )

    override fun addExtraModels() {
        groupDelegate?.addGroupCarousel(this) // don't remove the null-check or it would crash...
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, scope: CoroutineScope): RecommendPageController
    }
}
