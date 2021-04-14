package app.melon.event.ui

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.resultentities.EventAndOrganiser
import app.melon.event.EventDetailActivity
import app.melon.user.api.IUserService
import app.melon.util.time.MelonDateTimeFormatter
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class EventsPageController @AssistedInject constructor(
    @Assisted context: Context,
    private val userService: IUserService,
    private val dateTimeFormatter: MelonDateTimeFormatter
) : BasePagingController<EventAndOrganiser>(context) {

    override fun buildItemModel(currentPosition: Int, item: EventAndOrganiser?): EpoxyModel<*> {
        return EventItem_()
            .id("nearby_event_$currentPosition")
            .item(item!!)
            .formatter(dateTimeFormatter)
            .detailEntryClickListener { EventDetailActivity.start(context, it.event.id, it) }
            .profileEntryClickListener { userService.navigateToUserProfile(context, it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context): EventsPageController
    }
}