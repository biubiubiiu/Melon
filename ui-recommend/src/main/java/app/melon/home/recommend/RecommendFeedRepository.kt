package app.melon.home.recommend

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.data.MelonDatabase
import app.melon.data.daos.RecommendedDao
import app.melon.data.resultentities.RecommendedEntryWithFeed
import app.melon.data.services.FeedApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class RecommendFeedRepository @Inject constructor(
    private val recommendedDao: RecommendedDao,
    private val service: FeedApiService,
    private val database: MelonDatabase
) {

    fun fetchData(timestamp: Long): Flow<PagingData<RecommendedEntryWithFeed>> {
        return Pager(
            config = PAGING_CONFIG,
            remoteMediator = RecommendRemoteMediator(
                timestamp,
                service,
                database
            ),
            pagingSourceFactory = { recommendedDao.feedDataSource() }
        ).flow
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 20,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}