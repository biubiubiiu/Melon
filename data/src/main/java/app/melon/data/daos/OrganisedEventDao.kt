package app.melon.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.melon.data.daos.base.EntityDao
import app.melon.data.entities.OrganisedEvent
import app.melon.data.resultentities.RecommendedEntryWithFeed


@Dao
abstract class OrganisedEventDao : EntityDao<OrganisedEvent>() {

    @Transaction
    @Query("SELECT * FROM organised_event")
    abstract fun eventsDataSource(): PagingSource<Int, OrganisedEvent>

    @Query("SELECT * FROM organised_event WHERE event_id = :id")
    abstract fun getEventWithId(id: String): OrganisedEvent?

    @Query("DELETE FROM organised_event WHERE event_id = :id")
    abstract fun deleteEventWithId(id: String)

    @Query("DELETE FROM organised_event")
    abstract fun deleteAll()
}