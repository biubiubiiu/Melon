package app.melon.event.detail

import app.melon.data.entities.Event

data class EventDetailViewState(
    val id: String,
    val pageItem: Event? = null,
    val refreshing: Boolean = false,
    val changingEventState: Boolean = false
)