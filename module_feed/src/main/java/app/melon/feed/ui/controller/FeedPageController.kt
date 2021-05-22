package app.melon.feed.ui.controller

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.resultentities.FeedAndAuthor
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class FeedPageController @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted private val type: Type, // TODO move this field to [Feed]
    @Assisted private val idProvider: (FeedAndAuthor?, Int) -> String,
    private val factory: FeedControllerDelegate.Factory
) : BasePagingController<FeedAndAuthor>(
    context,
    sameItemIndicator = { oldItem, newItem -> oldItem.feed.id == newItem.feed.id }
) {

    enum class Type {
        NORMAL, ANONYMOUS
    }

    private val delegate by lazy {
        factory.create(context)
    }

    override fun buildItemModel(currentPosition: Int, item: FeedAndAuthor?): EpoxyModel<*> =
        when (type) {
            Type.NORMAL -> delegate.buildFeedItem(
                dataProvider = { item!! },
                idProvider = { idProvider(item, currentPosition) }
            )
            Type.ANONYMOUS -> delegate.buildAnonymousFeedItem(
                dataProvider = { item!! },
                idProvider = { idProvider(item, currentPosition) }
            )
        }


    @AssistedFactory
    interface Factory {
        fun create(
            context: Context,
            idProvider: (FeedAndAuthor?, Int) -> String,
            type: Type = Type.NORMAL // TODO we don't need this as it should be retrieved from Feed.type
        ): FeedPageController
    }
}