package app.melon.data.daos

import androidx.room.Dao
import androidx.room.Query
import app.melon.data.daos.base.EntityDao
import app.melon.data.entities.AttendEvent


@Dao
abstract class AttendEventDao : EntityDao<AttendEvent>() {

    @Query("SELECT * FROM attend_event WHERE event_id = :eventId AND user_id = :userId")
    abstract suspend fun getAttendEventRecord(eventId: String, userId: String): AttendEvent?

    @Query("SELECT * FROM attend_event WHERE user_id = :userId")
    abstract suspend fun getAttendEventRecord(userId: String): List<AttendEvent>

    suspend fun insert(eventId: String, userId: String) = insert(
        AttendEvent(
            eventId,
            userId
        )
    )

    @Query("DELETE FROM attend_event WHERE event_id = :eventId AND user_id = :userId")
    abstract suspend fun delete(eventId: String, userId: String)

    override suspend fun insertOrUpdate(entity: AttendEvent) {
        val item = getAttendEventRecord(entity.eventId, entity.userId)
        if (item == null) {
            insert(entity)
        } else {
            update(entity)
        }
    }
}