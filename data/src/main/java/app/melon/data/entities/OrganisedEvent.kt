package app.melon.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "organised_event",
    indices = [
        Index(value = ["event_id"], unique = true)
    ]
)
/**
 * We don't use embedded [Event] here cause we want to
 * save a **minimal** information of joined events
 */
data class OrganisedEvent(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "event_id") val eventId: String = "",
    @ColumnInfo(name = "event_type") val eventType: String? = null,
    @ColumnInfo(name = "event_title") val eventTitle: String = "",
    @ColumnInfo(name = "event_content") val eventContent: String = "",
    @ColumnInfo(name = "event_start_time") val eventStartTime: String = "",
    @ColumnInfo(name = "event_end_time") val eventEndTime: String = "",
    @ColumnInfo(name = "event_location") val eventLocation: String = "",
    @ColumnInfo(name = "organiser_id") val organiserId: String = "",
    @ColumnInfo(name = "organiser_username") val organiserUsername: String = "",
    @ColumnInfo(name = "organiser_avatar_url") val organiserAvatarUrl: String = "",
    @ColumnInfo(name = "organiser_school") val organiserSchool: String? = null
) : MelonEntity {

    companion object {
        fun from(event: Event): OrganisedEvent {
            return OrganisedEvent(
                id = 0,
                eventId = event.id,
                eventType = event.type,
                eventTitle = event.title,
                eventContent = event.content,
                eventStartTime = event.startTime,
                eventEndTime = event.endTime,
                eventLocation = event.location,
                organiserId = event.organiser!!.id,
                organiserUsername = event.organiser.username,
                organiserAvatarUrl = event.organiser.avatarUrl,
                organiserSchool = event.organiser.school
            )
        }
    }
}

fun OrganisedEvent.toEvent(): Event {
    return Event(
        id = this.eventId,
        type = this.eventType,
        title = this.eventTitle,
        content = this.eventContent,
        startTime = this.eventStartTime,
        endTime = this.eventEndTime,
        location = this.eventLocation,
        organiser = User(
            id = this.organiserId,
            username = this.organiserUsername,
            avatarUrl = this.organiserAvatarUrl,
            school = this.organiserSchool
        )
    )
}