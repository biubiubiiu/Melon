package app.melon.event.mine

import androidx.lifecycle.ViewModel
import app.melon.data.constants.JOINED_EVENTS
import app.melon.data.constants.ORGANISED_EVENTS
import app.melon.event.interactor.UpdateEventList
import javax.inject.Inject


class MyEventsViewModel @Inject constructor(
    updateOrganisedEvents: UpdateEventList,
    updateJoiningEvents: UpdateEventList
) : ViewModel() {

    val organisedEvents = updateOrganisedEvents.observe()
    val joiningEvents = updateJoiningEvents.observe()

    init {
        updateOrganisedEvents(UpdateEventList.Params(ORGANISED_EVENTS))
        updateJoiningEvents(UpdateEventList.Params(JOINED_EVENTS))
    }
}