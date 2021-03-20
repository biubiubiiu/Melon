package app.melon.home.following

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.data.MelonDatabase
import app.melon.data.daos.FollowingDao
import app.melon.data.resultentities.FollowingEntryWithFeed
import app.melon.data.services.FeedApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FollowFeedRepository @Inject constructor(
    private val followingDao: FollowingDao,
    private val service: FeedApiService,
    private val database: MelonDatabase
) {

    fun fetchData(timestamp: Long): Flow<PagingData<FollowingEntryWithFeed>> {
        return Pager(
            config = PAGING_CONFIG,
            remoteMediator = FollowRemoteMediator(
                timestamp,
                service,
                database
            ),
            pagingSourceFactory = { followingDao.feedDataSource() }
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