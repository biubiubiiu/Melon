package app.melon.feed.ui

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.melon.data.constants.MY_ANONYMOUS_POST
import app.melon.data.constants.MY_FAVORITE_POST
import app.melon.data.constants.MY_POST
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.data.FeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FeedListViewModel @Inject constructor(
    private val repo: FeedRepository
) : ViewModel() {

    fun refresh(queryType: Int, param: Bundle?): Flow<PagingData<FeedAndAuthor>> {
        return when (queryType) {
            MY_POST, MY_ANONYMOUS_POST, MY_FAVORITE_POST -> {
                val fakeUid = "6" // TODO
                repo.getFeedsFromUser(fakeUid).cachedIn(viewModelScope)
            }
            else -> {
                val timestamp = 100000L // TODO
                repo.getFeedList(timestamp, queryType).cachedIn(viewModelScope)
            }
        }
    }
}