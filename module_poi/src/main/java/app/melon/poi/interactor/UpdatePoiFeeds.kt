package app.melon.poi.interactor

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.base.domain.PagingInteractor
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.data.FeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


internal class UpdatePoiFeeds @Inject constructor(
    private val repo: FeedRepository
) : PagingInteractor<UpdatePoiFeeds.Params, FeedAndAuthor>() {

    override fun createObservable(params: Params): Flow<PagingData<FeedAndAuthor>> {
        return repo.getPoiFeeds(params.id, params.pagingConfig)
    }

    internal data class Params(
        val id: String,
        override val pagingConfig: PagingConfig = PAGING_CONFIG
    ) : Parameters<FeedAndAuthor>

    private companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 8,
            initialLoadSize = 16,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}