package app.melon.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.melon.data.daos.base.PaginatedEntryDao
import app.melon.data.entities.RecommendedFeedEntry
import app.melon.data.resultentities.RecommendedEntryWithFeed

@Dao
abstract class RecommendedDao : PaginatedEntryDao<RecommendedFeedEntry, RecommendedEntryWithFeed>() {

    @Transaction
    @Query("SELECT * FROM recommended_feeds ORDER BY page ASC, page_order ASC")
    abstract fun feedDataSource(): PagingSource<Int, RecommendedEntryWithFeed>

    @Query("DELETE FROM recommended_feeds WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM recommended_feeds")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) FROM recommended_feeds")
    abstract override suspend fun getLastPage(): Int?
}