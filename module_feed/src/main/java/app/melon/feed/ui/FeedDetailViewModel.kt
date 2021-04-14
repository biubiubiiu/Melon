package app.melon.feed.ui

import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ReduxViewModel
import app.melon.comment.interactor.UpdateCommentList
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.interactors.UpdateFeedDetail
import app.melon.feed.ui.state.FeedDetailViewState
import app.melon.util.base.ErrorResult
import app.melon.util.base.Success
import kotlinx.coroutines.launch
import javax.inject.Inject


class FeedDetailViewModel @Inject constructor(
    private val updateFeedDetail: UpdateFeedDetail,
    private val updateComments: UpdateCommentList
) : ReduxViewModel<FeedDetailViewState>(
    initialState = FeedDetailViewState()
) {

    val commentsPagingData = updateComments.observe()

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
    }

    fun refresh(item: FeedAndAuthor) {
        viewModelScope.launchSetState {
            copy(pageItem = item)
        }
        viewModelScope.launch {
            updateFeedDetail(UpdateFeedDetail.Params(item.feed.id))
        }
        viewModelScope.launch {
            updateComments(UpdateCommentList.Params(item.feed.id))
        }
    }
}