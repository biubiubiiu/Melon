package app.melon.event

import android.content.Context
import app.melon.event.api.IEventService

class EventService : IEventService {

    override fun navigateToEventList(context: Context) {
        NearbyEventsActivity.start(context)
    }
}