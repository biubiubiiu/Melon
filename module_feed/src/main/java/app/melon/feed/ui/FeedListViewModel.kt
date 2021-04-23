package app.melon.feed.ui

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.melon.base.framework.ReduxViewModel
import app.melon.util.timesync.TimeSyncer
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.interactors.UpdateFeedList
import app.melon.util.base.ErrorResult
import app.melon.util.base.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class FeedListViewModel @Inject constructor(
    private val timeSyncer: TimeSyncer,
    private val updateFeedList: UpdateFeedList
) : ReduxViewModel<FeedListViewState>(
    initialState = FeedListViewState()
) {

    val pagingData: Flow<PagingData<FeedAndAuthor>> = updateFeedList.observe().cachedIn(viewModelScope)

    fun refresh(queryType: Int, param: Bundle?) {
        viewModelScope.launch {
            when (val result = timeSyncer.getServerTime()) {
                is Success -> updateFeedList(UpdateFeedList.Params(queryType, result.get(), param))
                is ErrorResult -> setState {
                    copy(error = result.throwable) // fail fast if we cant get sync time with server
                }
            }
        }
    }
}