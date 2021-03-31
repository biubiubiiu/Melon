package app.melon.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.melon.data.daos.base.PaginatedEntryDao
import app.melon.data.entities.ANExploreFeedEntry
import app.melon.data.resultentities.ANExploreEntryWithFeed

@Dao
abstract class ANExploreDao : PaginatedEntryDao<ANExploreFeedEntry, ANExploreEntryWithFeed>() {

    @Transaction
    @Query("SELECT * FROM anonymous_explore_feeds ORDER BY page ASC, page_order ASC")
    abstract fun feedDataSource(): PagingSource<Int, ANExploreEntryWithFeed>

    @Query("DELETE FROM anonymous_explore_feeds WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM anonymous_explore_feeds")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) FROM anonymous_explore_feeds")
    abstract override suspend fun getLastPage(): Int?
}