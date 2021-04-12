package app.melon.event.nearby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.melon.data.entities.Event
import app.melon.event.data.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NearbyEventsViewModel @Inject constructor(
    private val repo: EventRepository
) : ViewModel() {

    fun getStream(): Flow<PagingData<Event>> {
        return repo.getStream().cachedIn(viewModelScope)
    }
}