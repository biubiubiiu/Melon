package app.melon.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.melon.data.daos.base.PaginatedEntryDao
import app.melon.data.entities.FollowingFeedEntry
import app.melon.data.resultentities.FollowingEntryWithFeed

@Dao
abstract class FollowingDao : PaginatedEntryDao<FollowingFeedEntry, FollowingEntryWithFeed>() {

    @Transaction
    @Query("SELECT * FROM following_feeds ORDER BY page ASC, page_order ASC")
    abstract fun feedDataSource(): PagingSource<Int, FollowingEntryWithFeed>

    @Query("DELETE FROM following_feeds WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM following_feeds")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) FROM following_feeds")
    abstract override suspend fun getLastPage(): Int?
}