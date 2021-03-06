package app.melon.feed.ui.controller

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.comment.CommentControllerDelegate
import app.melon.data.resultentities.CommentAndAuthor
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.FeedActionHandler
import app.melon.feed.ui.widget.feedHeader
import app.melon.location.LocationHelper
import app.melon.util.formatter.MelonDateTimeFormatter
import app.melon.util.formatter.MelonDistanceFormatter
import app.melon.util.formatter.MelonNumberFormatter
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class FeedDetailController @AssistedInject constructor(
    @Assisted context: Context,
    commentControllerFactory: CommentControllerDelegate.Factory,
    actionHandlerFactory: FeedActionHandler.Factory,
    private val locationHelper: LocationHelper,
    private val numberFormatter: MelonNumberFormatter,
    private val dateTimeFormatter: MelonDateTimeFormatter,
    private val distanceFormatter: MelonDistanceFormatter
) : BasePagingController<CommentAndAuthor>(
    context,
    sameItemIndicator = { oldItem, newItem -> oldItem.comment.id == newItem.comment.id }
) {

    internal var item: FeedAndAuthor? = null
        set(value) {
            field = value
            requestModelBuild()
        }

    private val actionHandler = actionHandlerFactory.create(context)
    private val commentDelegate = commentControllerFactory.create(context, this)

    override fun addExtraModels() {
        item?.let { item ->
            feedHeader {
                id("feed_detail_header")
                item(item)
                locationHelper(locationHelper)
                numberFormatter(numberFormatter)
                distanceFormatter(distanceFormatter)
                formatter(dateTimeFormatter)
                actions(actionHandler)
            }
        }
    }

    override fun buildItemModel(currentPosition: Int, item: CommentAndAuthor?): EpoxyModel<*> {
        return commentDelegate.buildCommentItem(
            dataProvider = { item!! },
            idProvider = { "comment_$currentPosition" }
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context): FeedDetailController
    }
}