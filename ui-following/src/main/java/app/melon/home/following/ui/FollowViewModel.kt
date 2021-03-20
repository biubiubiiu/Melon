package app.melon.home.following.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.melon.data.resultentities.FollowingEntryWithFeed
import app.melon.home.following.FollowFeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FollowViewModel @Inject constructor(
    private val repository: FollowFeedRepository
) : ViewModel() {

    fun refresh(timestamp: Long): Flow<PagingData<FollowingEntryWithFeed>> {
        return repository.fetchData(timestamp).cachedIn(viewModelScope)
    }
}