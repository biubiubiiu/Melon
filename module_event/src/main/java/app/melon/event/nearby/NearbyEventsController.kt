package app.melon.event.nearby

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.entities.Event
import app.melon.event.ui.EventItem_
import app.melon.user.api.IUserService
import app.melon.util.time.MelonDateTimeFormatter
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class NearbyEventsController @AssistedInject constructor(
    @Assisted context: Context,
    private val userService: IUserService,
    private val dateTimeFormatter: MelonDateTimeFormatter
) : BasePagingController<Event>(context) {

    override fun buildItemModel(currentPosition: Int, item: Event?): EpoxyModel<*> {
        return EventItem_()
            .id("nearby_event_$currentPosition")
            .item(item!!)
            .formatter(dateTimeFormatter)
            .detailEntryClickListener { }
            .profileEntryClickListener { userService.navigateToUserProfile(context, it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context): NearbyEventsController
    }
}