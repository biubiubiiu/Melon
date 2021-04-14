package app.melon.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.melon.data.constants.EventPageType
import app.melon.data.daos.base.TypedPaginatedEntryDao
import app.melon.data.entities.EventEntry
import app.melon.data.resultentities.EntryWithEventAndOrganiser


@Dao
abstract class EventEntryDao : TypedPaginatedEntryDao<EventEntry, Int>() {

    @Transaction
    @Query("SELECT * FROM event_entries WHERE page_type = :type ORDER BY page ASC, page_order ASC")
    abstract fun eventDataSource(@EventPageType type: Int): PagingSource<Int, EntryWithEventAndOrganiser>

    @Query("SELECT * FROM event_entries WHERE event_id = :id AND page_type = :type")
    abstract suspend fun entryWithIdAndType(id: String, @EventPageType type: Int): EventEntry?

    @Query("SELECT event_id FROM event_entries WHERE page_type = :type")
    abstract suspend fun eventIdWithType(@EventPageType type: Int): List<String>

    @Query("DELETE FROM event_entries WHERE page = :page AND page_type = :type")
    abstract override suspend fun deletePage(page: Int, @EventPageType type: Int)

    @Query("DELETE FROM event_entries WHERE page_type = :type")
    abstract override suspend fun deleteEntryByType(@EventPageType type: Int)

    @Query("SELECT MAX(page) FROM event_entries WHERE page_type = :type")
    abstract override suspend fun getLastPage(@EventPageType type: Int): Int?
}