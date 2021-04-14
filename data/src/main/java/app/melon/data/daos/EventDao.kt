package app.melon.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.melon.data.daos.base.EntityDao
import app.melon.data.entities.Event
import app.melon.data.resultentities.EventAndOrganiser


@Dao
abstract class EventDao : EntityDao<Event>() {

    @Query("SELECT * FROM events WHERE event_id = :id")
    abstract suspend fun getEventWithId(id: String): Event?

    suspend fun getEventWithIdOrThrow(id: String): Event {
        return getEventWithId(id) ?: throw IllegalArgumentException("No event with id $id in database")
    }

    @Transaction
    @Query("SELECT * FROM events WHERE event_id = :id")
    abstract suspend fun getEventAndOrganiserWithId(id: String): EventAndOrganiser?

    @Query("DELETE FROM events WHERE event_id = :id")
    abstract suspend fun delete(id: String)

    @Transaction
    open suspend fun deleteAll(ids: List<String>) {
        ids.forEach {
            delete(it)
        }
    }

    @Query("DELETE FROM events")
    abstract suspend fun deleteAll()

    override suspend fun insertOrUpdate(entity: Event) {
        val item = getEventWithId(entity.id)
        if (item == null) {
            insert(entity)
        } else {
            update(entity)
        }
    }
}