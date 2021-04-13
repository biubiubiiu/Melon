package app.melon.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.melon.data.daos.base.EntityDao
import app.melon.data.entities.JoiningEvent


@Dao
abstract class EventDao : EntityDao<JoiningEvent>() {

    @Transaction
    @Query("SELECT * FROM joining_event")
    abstract fun eventsDataSource(): PagingSource<Int, JoiningEvent>

    @Query("SELECT * FROM joining_event WHERE event_id = :id")
    abstract fun getEventWithId(id: String): JoiningEvent?

    @Query("DELETE FROM joining_event WHERE event_id = :id")
    abstract fun deleteEventWithId(id: String)

    @Query("DELETE FROM joining_event")
    abstract fun deleteAll()
}