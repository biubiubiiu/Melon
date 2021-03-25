package app.melon.user.interactor

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.data.entities.Feed
import app.melon.base.domain.PagingInteractor
import app.melon.user.data.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateUserFeeds @Inject constructor(
    private val repository: UserProfileRepository
) : PagingInteractor<UpdateUserFeeds.Params, Feed>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Feed>> {
        return repository.getFeedsFromUser(params.uid, params.pagingConfig)
    }

    data class Params(
        val uid: String,
        override val pagingConfig: PagingConfig = PAGING_CONFIG
    ) : Parameters<Feed>

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