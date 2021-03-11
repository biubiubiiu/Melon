package app.melon.domain.observers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.data.daos.RecommendedDao
import app.melon.data.resultentities.RecommendedEntryWithFeed
import app.melon.domain.PaginatedEntryRemoteMediator
import app.melon.domain.PagingInteractor
import app.melon.domain.interactors.UpdateRecommendedFeeds
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ObservePagedRecommendedFeeds @Inject constructor(
    private val recommendedDao: RecommendedDao,
    private val updateRecommendedFeeds: UpdateRecommendedFeeds
) : PagingInteractor<ObservePagedRecommendedFeeds.Params, RecommendedEntryWithFeed>() {

    override fun createObservable(params: Params): Flow<PagingData<RecommendedEntryWithFeed>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PaginatedEntryRemoteMediator { page ->
                updateRecommendedFeeds.executeSync(
                    UpdateRecommendedFeeds.Params(
                        page = page,
                        pageSize = params.pagingConfig.pageSize,
                        timestamp = 100000 // TODO 同步服务器时间
                    )
                )
            },
            pagingSourceFactory = recommendedDao::feedDataSource
        ).flow
    }

    data class Params(
        override val pagingConfig: PagingConfig
    ) : Parameters<RecommendedEntryWithFeed>
}