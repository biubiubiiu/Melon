package app.melon.home.following.ui

import androidx.lifecycle.ViewModel
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.data.resultentities.FollowingEntryWithFeed
import app.melon.domain.observers.ObservePagedFollowingFeeds
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FollowViewModel @Inject constructor(
    private val pagingInteractor: ObservePagedFollowingFeeds
) : ViewModel() {

    init {
        pagingInteractor(ObservePagedFollowingFeeds.Params(PAGING_CONFIG, forceRefresh = true))
    }

    val pagingData: Flow<PagingData<FollowingEntryWithFeed>>
        get() = pagingInteractor.observe()

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 20,
            prefetchDistance = 8,
            enablePlaceholders = false
        )
    }
}