package app.melon.event.detail

import app.melon.data.resultentities.EventAndOrganiser

data class EventDetailViewState(
    val id: String,
    val pageItem: EventAndOrganiser? = null,
    val refreshing: Boolean = false,
    val changingEventState: Boolean = false,
    val error: Throwable? = null
)