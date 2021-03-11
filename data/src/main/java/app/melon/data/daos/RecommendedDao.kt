package app.melon.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import app.melon.data.daos.base.PaginatedEntryDao
import app.melon.data.entities.RecommendFeedEntry
import app.melon.data.resultentities.RecommendedEntryWithFeed
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RecommendedDao : PaginatedEntryDao<RecommendFeedEntry, RecommendedEntryWithFeed>() {

    @Query("SELECT * FROM recommended_feeds WHERE page = :page ORDER BY page_order")
    abstract fun entriesObservable(page: Int): Flow<List<RecommendFeedEntry>>

    @Query("SELECT * FROM recommended_feeds ORDER BY page ASC, id ASC")
    abstract fun feedDataSource(): PagingSource<Int, RecommendedEntryWithFeed>

    @Query("DELETE FROM recommended_feeds WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM recommended_feeds")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) FROM recommended_feeds")
    abstract override suspend fun getLastPage(): Int?
}