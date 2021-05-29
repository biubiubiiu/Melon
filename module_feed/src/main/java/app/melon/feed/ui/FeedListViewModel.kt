package app.melon.feed.ui

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import app.melon.base.framework.ReduxViewModel
import app.melon.feed.interactors.UpdateFeedList
import app.melon.util.timesync.TimeSyncer
import kotlinx.coroutines.launch
import javax.inject.Inject


class FeedListViewModel @Inject constructor(
    private val timeSyncer: TimeSyncer,
    private val updateFeedList: UpdateFeedList
) : ReduxViewModel<FeedListViewState>(
    initialState = FeedListViewState()
) {

    val pagingData = updateFeedList.observe().cachedIn(viewModelScope)

    fun refresh(queryType: Int, param: Bundle?) {
        viewModelScope.launch {
            timeSyncer.getServerTime().onSuccess { time ->
                updateFeedList(UpdateFeedList.Params(queryType, time, param))
            }.onFailure { throwable ->
                setState {
                    copy(error = throwable) // fail fast if we cant get sync time with server
                }
            }
        }
    }
}