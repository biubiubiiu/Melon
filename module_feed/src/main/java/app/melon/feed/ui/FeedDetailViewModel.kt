package app.melon.feed.ui

import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ReduxViewModel
import app.melon.comment.interactor.UpdateComments
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.interactors.UpdateFeedDetail
import app.melon.feed.ui.state.FeedDetailViewState
import app.melon.util.base.ErrorResult
import app.melon.util.base.Success
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class FeedDetailViewModel @Inject constructor(
    private val updateFeedDetail: UpdateFeedDetail,
    private val updateComments: UpdateComments
) : ReduxViewModel<FeedDetailViewState>(
    initialState = FeedDetailViewState()
) {

    private var commentPage = 0
    private var loadingMore = false
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
                    is Success -> copy(pageItem = it.get())
                    is ErrorResult -> copy(error = it.throwable)
                }
            }
        }
        viewModelScope.launch {
            updateComments.observe().collectAndSetState {
                copy(comments = it)
            }
        }
        viewModelScope.launch {
            updateComments.loadingMore.observable.collect {
                loadingMore = it
            }
        }
        selectSubscribeDistinct(FeedDetailViewState::pageItem) {
            if (it != null) {
                feedId = it.feed.id
            }
        }
    }

    fun refresh(item: FeedAndAuthor) {
        viewModelScope.launchSetState {
            copy(pageItem = item)
        }
        viewModelScope.launch {
            updateFeedDetail(UpdateFeedDetail.Params(item.feed.id))
        }
        commentPage = 0
        viewModelScope.launch {
            updateComments(UpdateComments.Params(item.feed.id, commentPage))
        }
    }

    fun loadMoreComment() {
        if (loadingMore) {
            return
        }
        commentPage += 1
        viewModelScope.launch {
            updateComments(UpdateComments.Params(feedId, commentPage))
        }
    }
}