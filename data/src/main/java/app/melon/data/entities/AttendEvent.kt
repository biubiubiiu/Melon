package app.melon.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(
    tableName = "attend_event",
    primaryKeys = ["event_id", "user_id"]
)
data class AttendEvent(
    @ColumnInfo(name = "event_id") val eventId: String,
    @ColumnInfo(name = "user_id") val userId: String
)