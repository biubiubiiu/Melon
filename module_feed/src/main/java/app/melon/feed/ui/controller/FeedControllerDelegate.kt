package app.melon.feed.ui.controller

import android.content.Context
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.FeedActionHandler
import app.melon.feed.ui.widget.AnonymousFeedItem_
import app.melon.feed.ui.widget.FeedItem_
import app.melon.location.LocationHelper
import app.melon.util.formatter.MelonDateTimeFormatter
import app.melon.util.formatter.MelonDistanceFormatter
import app.melon.util.formatter.MelonNumberFormatter
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class FeedControllerDelegate @AssistedInject constructor(
    @Assisted context: Context,
    actionHandlerFactory: FeedActionHandler.Factory,
    private val locationHelper: LocationHelper,
    private val dateTimeFormatter: MelonDateTimeFormatter,
    private val numberFormatter: MelonNumberFormatter,
    private val distanceFormatter: MelonDistanceFormatter
) {

    private val actionHandler = actionHandlerFactory.create(context)

    fun buildFeedItem(
        dataProvider: () -> FeedAndAuthor,
        idProvider: () -> String = { "feed_${dataProvider.invoke().feed.id}" }
    ): EpoxyModel<*> {
        val item = dataProvider.invoke()
        return FeedItem_()
            .id(idProvider.invoke())
            .item(item)
            .locationHelper(locationHelper)
            .formatter(dateTimeFormatter)
            .numberFormatter(numberFormatter)
            .distanceFormatter(distanceFormatter)
            .actions(actionHandler)
    }

    fun buildAnonymousFeedItem(
        dataProvider: () -> FeedAndAuthor,
        idProvider: () -> String = { "feed_${dataProvider.invoke().feed.id}" }
    ): EpoxyModel<*> {
        val item = dataProvider.invoke()
        return AnonymousFeedItem_()
            .id(idProvider.invoke())
            .item(item)
            .formatter(dateTimeFormatter)
            .numberFormatter(numberFormatter)
            .actions(actionHandler)
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context): FeedControllerDelegate
    }
}