package app.melon.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.melon.data.constants.FeedPageType
import app.melon.data.daos.base.TypedPaginatedEntryDao
import app.melon.data.entities.FeedEntry
import app.melon.data.resultentities.EntryWithFeedAndAuthor


@Dao
abstract class FeedEntryDao : TypedPaginatedEntryDao<FeedEntry, Int>() {

    @Transaction
    @Query("SELECT * FROM feed_entries WHERE page_type = :type ORDER BY page ASC, page_order ASC")
    abstract fun feedDataSource(@FeedPageType type: Int): PagingSource<Int, EntryWithFeedAndAuthor>

    @Query("SELECT * FROM feed_entries WHERE feed_id = :id AND page_type = :type")
    abstract suspend fun entryWithIdAndType(id: String, @FeedPageType type: Int): FeedEntry?

    @Query("SELECT feed_id FROM feed_entries WHERE page_type = :type")
    abstract suspend fun feedIdWithType(@FeedPageType type: Int): List<String>

    @Query("DELETE FROM feed_entries WHERE page = :page AND page_type = :type")
    abstract override suspend fun deletePage(page: Int, @FeedPageType type: Int)

    @Query("DELETE FROM feed_entries WHERE page_type = :type")
    abstract override suspend fun deleteEntryByType(@FeedPageType type: Int)

    @Query("SELECT MAX(page) FROM feed_entries WHERE page_type = :type")
    abstract override suspend fun getLastPage(@FeedPageType type: Int): Int?
}