package app.melon.home.following.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.melon.data.constants.FOLLOWING_FEED
import app.melon.data.constants.FeedPageType
import app.melon.data.resultentities.EntryWithFeedAndAuthor
import app.melon.feed.data.FeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FollowViewModel @Inject constructor(
    private val repository: FeedRepository
) : ViewModel() {

    @FeedPageType private val mQueryType = FOLLOWING_FEED

    fun refresh(timestamp: Long): Flow<PagingData<EntryWithFeedAndAuthor>> {
        return repository.getFeedList(timestamp, mQueryType).cachedIn(viewModelScope)
    }
}