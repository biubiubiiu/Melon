package app.melon.data.entities

import androidx.annotation.StringDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "events",
    indices = [
        Index(value = ["organiser_id"], unique = false)
    ]
)
data class Event(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "event_id") override val id: String = "",
    @ColumnInfo(name = "organiser_id") val organiserUid: String? = null,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "content") val content: String? = null,
    @ColumnInfo(name = "type") val type: String? = null,
    @ColumnInfo(name = "start_time") val startTime: String? = null,
    @ColumnInfo(name = "end_time") val endTime: String? = null,
    @ColumnInfo(name = "location") val location: String? = null,
    @ColumnInfo(name = "cost") val cost: Float? = null,
    @ColumnInfo(name = "demand") val demand: String? = null,
    @ColumnInfo(name = "status") @EventStatus val status: String? = null
) : MelonEntity {
    val isIdle get() = status == IDLE
    val isJoining get() = status == JOINING
    val isExclude get() = status == EXCLUDED
    val isExpired get() = status == EXPIRED
}

const val IDLE = "idle"
const val JOINING = "joining"
const val EXCLUDED = "excluded"
const val EXPIRED = "expired"

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    IDLE,
    JOINING,
    EXCLUDED,
    EXPIRED
)
annotation class EventStatus