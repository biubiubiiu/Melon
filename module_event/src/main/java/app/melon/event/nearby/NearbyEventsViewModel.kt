package app.melon.event.nearby

import androidx.lifecycle.ViewModel
import app.melon.data.constants.ALL_EVENTS
import app.melon.event.interactor.UpdateEventList
import javax.inject.Inject


class NearbyEventsViewModel @Inject constructor(
    updateNearbyEvents: UpdateEventList
) : ViewModel() {

    val eventsPagingData = updateNearbyEvents.observe()

    init {
        updateNearbyEvents(UpdateEventList.Params(ALL_EVENTS))
    }
}