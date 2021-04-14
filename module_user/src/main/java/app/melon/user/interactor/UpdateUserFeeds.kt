package app.melon.user.interactor

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.base.domain.PagingInteractor
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.user.data.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateUserFeeds @Inject constructor(
    private val repository: UserRepository
) : PagingInteractor<UpdateUserFeeds.Params, FeedAndAuthor>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<FeedAndAuthor>> {
        return repository.getFeedsFromUser(params.uid, params.pagingConfig)
    }

    data class Params(
        val uid: String,
        override val pagingConfig: PagingConfig = PAGING_CONFIG
    ) : Parameters<FeedAndAuthor>

    companion object {
        private const val PAGE_SIZE = 8
        private val PAGING_CONFIG = PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = 16,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}