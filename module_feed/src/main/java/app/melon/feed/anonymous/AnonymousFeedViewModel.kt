package app.melon.feed.anonymous

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.melon.data.resultentities.ANExploreEntryWithFeed
import app.melon.data.resultentities.ANSchoolEntryWithFeed
import app.melon.data.resultentities.ANTrendingEntryWithFeed
import app.melon.feed.data.FeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AnonymousFeedViewModel @Inject constructor(
    private val repository: FeedRepository
) : ViewModel() {

    fun reselect(position: Int) {

    }

    fun refreshSchoolFeeds(timestamp: Long): Flow<PagingData<ANSchoolEntryWithFeed>> {
        return repository.getAnonymousSchoolFeed(timestamp).cachedIn(viewModelScope)
    }

    fun refreshExploreFeeds(timestamp: Long): Flow<PagingData<ANExploreEntryWithFeed>> {
        return repository.getAnonymousExploreFeed(timestamp).cachedIn(viewModelScope)
    }

    fun refreshTrendingFeeds(timestamp: Long): Flow<PagingData<ANTrendingEntryWithFeed>> {
        return repository.getAnonymousTrendingFeed(timestamp).cachedIn(viewModelScope)
    }
}