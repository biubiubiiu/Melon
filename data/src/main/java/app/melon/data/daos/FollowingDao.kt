package app.melon.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import app.melon.data.daos.base.PaginatedEntryDao
import app.melon.data.entities.FollowingFeedEntry
import app.melon.data.resultentities.FollowingEntryWithFeed
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FollowingDao : PaginatedEntryDao<FollowingFeedEntry, FollowingEntryWithFeed>() {

    @Query("SELECT * FROM following_feeds WHERE page = :page ORDER BY page_order")
    abstract fun entriesObservable(page: Int): Flow<List<FollowingFeedEntry>>

    @Query("SELECT * FROM following_feeds ORDER BY page ASC, id ASC")
    abstract fun feedDataSource(): PagingSource<Int, FollowingEntryWithFeed>

    @Query("DELETE FROM following_feeds WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM following_feeds")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) FROM following_feeds")
    abstract override suspend fun getLastPage(): Int?
}