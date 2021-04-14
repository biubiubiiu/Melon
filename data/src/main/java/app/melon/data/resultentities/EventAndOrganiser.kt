package app.melon.data.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import app.melon.data.entities.Event
import app.melon.data.entities.User

data class EventAndOrganiser(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "organiser_id",
        entityColumn = "user_id"
    )
    val organiser: User
): ResultEntity