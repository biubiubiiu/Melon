package app.melon.data.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import app.melon.data.entities.Event
import app.melon.data.entities.EventEntry

data class EntryWithEventAndOrganiser(

    @Embedded val entry: EventEntry,
    @Relation(
        parentColumn = "event_id",
        entityColumn = "event_id",
        entity = Event::class
    )
    val compoundItem: EventAndOrganiser
)