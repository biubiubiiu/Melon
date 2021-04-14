package app.melon.feed.anonymous

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.melon.data.constants.ANONYMOUS_ALL_FEED
import app.melon.data.constants.ANONYMOUS_SCHOOL_FEED
import app.melon.data.constants.ANONYMOUS_TRENDING_FEED
import app.melon.data.resultentities.EntryWithFeedAndAuthor
import app.melon.feed.data.FeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


internal class AnonymousFeedViewModel @Inject constructor(
    private val repository: FeedRepository
) : ViewModel() {

    fun reselect(position: Int) {

    }

    fun refreshSchoolFeeds(timestamp: Long): Flow<PagingData<EntryWithFeedAndAuthor>> {
        return repository.getFeedList(timestamp, ANONYMOUS_SCHOOL_FEED).cachedIn(viewModelScope)
    }

    fun refreshExploreFeeds(timestamp: Long): Flow<PagingData<EntryWithFeedAndAuthor>> {
        return repository.getFeedList(timestamp, ANONYMOUS_ALL_FEED).cachedIn(viewModelScope)
    }

    fun refreshTrendingFeeds(timestamp: Long): Flow<PagingData<EntryWithFeedAndAuthor>> {
        return repository.getFeedList(timestamp, ANONYMOUS_TRENDING_FEED).cachedIn(viewModelScope)
    }
}