package app.melon.event.detail

import android.content.Context
import app.melon.base.ui.list.refreshView
import app.melon.data.resultentities.EventAndOrganiser
import app.melon.event.ui.eventItem
import app.melon.user.api.IUserService
import app.melon.util.formatter.MelonDateTimeFormatter
import com.airbnb.epoxy.Typed2EpoxyController
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class EventDetailController @AssistedInject constructor(
    @Assisted private val context: Context,
    private val dateTimeFormatter: MelonDateTimeFormatter,
    private val userService: IUserService
) : Typed2EpoxyController<EventAndOrganiser, Boolean>() {

    override fun buildModels(item: EventAndOrganiser?, refreshing: Boolean) {
        if (refreshing && item == null) {
            refreshView {
                id("event_detail_refresh_view")
            }
        }
        if (item != null) {
            eventItem {
                id("event_detail_item")
                item(item)
                formatter(dateTimeFormatter)
                detailEntryClickListener { } // No-op
                profileEntryClickListener { userService.navigateToUserProfile(context, it) }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context): EventDetailController
    }
}