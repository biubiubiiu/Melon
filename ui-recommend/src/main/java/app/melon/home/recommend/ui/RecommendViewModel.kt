package app.melon.home.recommend.ui

import androidx.lifecycle.ViewModel
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.data.resultentities.RecommendedEntryWithFeed
import app.melon.domain.observers.ObservePagedRecommendedFeeds
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecommendViewModel @Inject constructor(
    private val pagingInteractor: ObservePagedRecommendedFeeds
) : ViewModel() {

    init {
        pagingInteractor(ObservePagedRecommendedFeeds.Params(PAGING_CONFIG))
    }

    val pagingData: Flow<PagingData<RecommendedEntryWithFeed>>
        get() = pagingInteractor.observe()

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 20,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}