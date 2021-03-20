package app.melon.home.recommend.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.melon.data.resultentities.RecommendedEntryWithFeed
import app.melon.home.recommend.RecommendFeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecommendViewModel @Inject constructor(
    private val repository: RecommendFeedRepository
) : ViewModel() {

    fun refresh(timestamp: Long): Flow<PagingData<RecommendedEntryWithFeed>> {
        return repository.fetchData(timestamp).cachedIn(viewModelScope)
    }
}