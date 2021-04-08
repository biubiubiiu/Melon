package app.melon.feed.ui

import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ReduxViewModel
import app.melon.data.entities.Feed
import app.melon.feed.interactors.UpdateComments
import app.melon.feed.interactors.UpdateFeedDetail
import app.melon.feed.ui.state.FeedDetailViewState
import app.melon.util.base.ErrorResult
import app.melon.util.base.Success
import kotlinx.coroutines.launch
import javax.inject.Inject


class FeedDetailViewModel @Inject constructor(
    private val updateFeedDetail: UpdateFeedDetail,
    private val updateComments: UpdateComments
) : ReduxViewModel<FeedDetailViewState>(
    initialState = FeedDetailViewState()
) {

    private var commentPage = 0
    private var feedId: String = ""

    init {
        viewModelScope.launch {
            updateFeedDetail.loadingState.observable.collectAndSetState {
                copy(refreshing = it)
            }
        }
        viewModelScope.launch {
            updateFeedDetail.observe().collectAndSetState {
                when (it) {
                    is Success -> copy(feed = it.get())
                    is ErrorResult -> copy(error = it.throwable)
                }
            }
        }
        viewModelScope.launch {
            updateComments.observe().collectAndSetState {
                copy(comments = it)
            }
        }
        selectSubscribeDistinct(FeedDetailViewState::feed) {
            if (it != null) {
                feedId = it.feedId
            }
        }
    }

    fun refresh(feed: Feed) {
        viewModelScope.launchSetState {
            copy(feed = feed)
        }
        viewModelScope.launch {
            updateFeedDetail(UpdateFeedDetail.Params(feed.feedId))
        }
        commentPage = 0
        viewModelScope.launch {
            updateComments(UpdateComments.Params(feed.feedId, commentPage))
        }
    }

    fun loadMoreComment() {
        commentPage += 1
        viewModelScope.launch {
            updateComments(UpdateComments.Params(feedId, commentPage))
        }
    }
}