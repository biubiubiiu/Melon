package app.melon.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.melon.data.daos.base.PaginatedEntryDao
import app.melon.data.entities.ANTrendingFeedEntry
import app.melon.data.resultentities.ANTrendingEntryWithFeed

@Dao
abstract class ANTrendingDao : PaginatedEntryDao<ANTrendingFeedEntry, ANTrendingEntryWithFeed>() {

    @Transaction
    @Query("SELECT * FROM anonymous_trending_feeds ORDER BY page ASC, page_order ASC")
    abstract fun feedDataSource(): PagingSource<Int, ANTrendingEntryWithFeed>

    @Query("DELETE FROM anonymous_trending_feeds WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM anonymous_trending_feeds")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) FROM anonymous_trending_feeds")
    abstract override suspend fun getLastPage(): Int?
}