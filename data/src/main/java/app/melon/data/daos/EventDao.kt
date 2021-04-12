package app.melon.data.daos

import androidx.room.Dao
import androidx.room.Query
import app.melon.data.daos.base.EntityDao
import app.melon.data.entities.JoiningEvent


@Dao
abstract class EventDao : EntityDao<JoiningEvent>() {

    @Query("SELECT * FROM joining_event WHERE event_id = :id")
    abstract fun getEventWithId(id: String): JoiningEvent?

    @Query("DELETE FROM joining_event WHERE event_id = :id")
    abstract fun deleteEventWithId(id: String)
}