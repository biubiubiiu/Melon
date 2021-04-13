package app.melon.event.mine

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import app.melon.data.entities.Event
import app.melon.event.interactor.UpdateEventList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class MyEventsViewModel @Inject constructor(
    private val updateOrganisedEvents: UpdateEventList,
    private val updateJoiningEvents: UpdateEventList
) : ViewModel() {

    val organisedEvents: Flow<PagingData<Event>> = updateOrganisedEvents.observe()
    val joiningEvents: Flow<PagingData<Event>> = updateJoiningEvents.observe()

    init {
        updateOrganisedEvents(UpdateEventList.Params(UpdateEventList.QueryType.ORGANISED))
        updateJoiningEvents(UpdateEventList.Params(UpdateEventList.QueryType.JOINING))
    }

    fun reselect(position: Int) {

    }
}