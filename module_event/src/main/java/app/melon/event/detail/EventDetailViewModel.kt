package app.melon.event.detail

import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ReduxViewModel
import app.melon.data.entities.Event
import app.melon.event.interactor.UpdateEventDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class EventDetailViewModel @AssistedInject constructor(
    @Assisted private val initialState: EventDetailViewState,
    private val updateEventDetail: UpdateEventDetail,
    private val changeEventStatus: ChangeEventStatus
) : ReduxViewModel<EventDetailViewState>(
    initialState = initialState
) {

    init {
        viewModelScope.launch {
            updateEventDetail.loadingState.observable.collectAndSetState {
                copy(refreshing = it)
            }
        }
        viewModelScope.launch {
            updateEventDetail.observe().collectAndSetState {
                copy(pageItem = it)
            }
        }
        viewModelScope.launch {
            changeEventStatus.loadingState.observable.collectAndSetState {
                copy(changingEventState = it)
            }
        }
        viewModelScope.launch {
            changeEventStatus.observe().collectAndSetState {
                copy(pageItem = it)
            }
        }
        selectSubscribeDistinct(EventDetailViewState::id) {
            updateEventDetail(it)
        }
    }

    fun joinOrLeaveEvent() {
        val item = currentState().pageItem ?: return
        viewModelScope.launch {
            changeEventStatus(
                ChangeEventStatus.Params(
                    event = item,
                    action = ChangeEventStatus.Action.TOGGLE
                )
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(state: EventDetailViewState): EventDetailViewModel
    }
}

fun EventDetailViewModel.Factory.create(
    id: String,
    cache: Event? = null
): EventDetailViewModel {
    val initialState = EventDetailViewState(id = id, pageItem = cache)
    return create(initialState)
}