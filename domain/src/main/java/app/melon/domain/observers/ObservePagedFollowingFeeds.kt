package app.melon.domain.observers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.data.daos.FollowingDao
import app.melon.data.resultentities.FollowingEntryWithFeed
import app.melon.domain.PaginatedEntryRemoteMediator
import app.melon.domain.PagingInteractor
import app.melon.domain.interactors.UpdateFollowingFeeds
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ObservePagedFollowingFeeds @Inject constructor(
    private val followingDao: FollowingDao,
    private val updateFollowingFeeds: UpdateFollowingFeeds
) : PagingInteractor<ObservePagedFollowingFeeds.Params, FollowingEntryWithFeed>() {

    override fun createObservable(params: Params): Flow<PagingData<FollowingEntryWithFeed>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PaginatedEntryRemoteMediator { page ->
                updateFollowingFeeds.executeSync(
                    UpdateFollowingFeeds.Params(
                        page = page,
                        pageSize = params.pagingConfig.pageSize,
                        forceRefresh = params.forceRefresh,
                        timestamp = 100000 // TODO 同步服务器时间
                    )
                )
            },
            pagingSourceFactory = followingDao::feedDataSource
        ).flow
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val forceRefresh: Boolean = false
    ) : Parameters<FollowingEntryWithFeed>
}