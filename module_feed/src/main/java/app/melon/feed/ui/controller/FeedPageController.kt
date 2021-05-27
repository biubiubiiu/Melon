package app.melon.feed.ui.controller

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.base.ui.list.EmptyView
import app.melon.base.ui.list.EmptyView_
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.FeedPageConfig
import app.melon.feed.R
import com.airbnb.epoxy.EpoxyModel


class FeedPageController internal constructor(
    context: Context,
    factory: FeedControllerDelegate.Factory,
    private val config: FeedPageConfig,
    private val type: Type = Type.NORMAL // TODO we don't need this as it should be retrieved from Feed.type
) : BasePagingController<FeedAndAuthor>(
    context,
    sameItemIndicator = { oldItem, newItem -> oldItem.feed.id == newItem.feed.id }
) {

    override val emptyView: EmptyView_
        get() = EmptyView_()
            .id(EmptyView::class.java.simpleName)
            .titleRes(config?.emptyViewTitleRes ?: R.string.empty_list_title)
            .subtitleRes(config?.emptyViewSubtitleRes ?: R.string.empty_list_subtitile)

    private val delegate = factory.create(context)

    enum class Type {
        NORMAL, ANONYMOUS
    }

    override fun buildItemModel(currentPosition: Int, item: FeedAndAuthor?): EpoxyModel<*> =
        when (type) {
            Type.NORMAL -> delegate.buildFeedItem(
                dataProvider = { item!! },
                idProvider = { "${config.idPrefix}_$currentPosition" }
            )
            Type.ANONYMOUS -> delegate.buildAnonymousFeedItem(
                dataProvider = { item!! },
                idProvider = { "${config.idPrefix}_$currentPosition" }
            )
        }
}