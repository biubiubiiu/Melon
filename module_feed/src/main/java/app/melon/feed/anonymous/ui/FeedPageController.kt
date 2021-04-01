package app.melon.feed.anonymous.ui

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.entities.Feed
import app.melon.feed.FeedControllerDelegate
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class FeedPageController @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted private val type: Type,
    @Assisted private val idProvider: (Feed?, Int) -> String,
    private val factory: FeedControllerDelegate.Factory
) : BasePagingController<Feed>(context) {

    enum class Type {
        NORMAL, ANONYMOUS
    }

    private val delegate by lazy {
        factory.create(context)
    }

    override fun buildItemModel(currentPosition: Int, item: Feed?): EpoxyModel<*> =
        when (type) {
            Type.NORMAL -> delegate.buildFeedItem(
                feedProvider = { item!! },
                idProvider = { idProvider(item, currentPosition) }
            )
            Type.ANONYMOUS -> delegate.buildAnonymousFeedItem(
                feedProvider = { item!! },
                idProvider = { idProvider(item, currentPosition) }
            )
        }


    @AssistedFactory
    interface Factory {
        fun create(
            context: Context,
            idProvider: (Feed?, Int) -> String,
            type: Type = Type.NORMAL // TODO we don't need this as it should decide from Feed.type
        ): FeedPageController
    }
}