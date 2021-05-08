package app.melon.feed.ui

import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ReduxViewModel
import app.melon.comment.interactor.UpdateCommentList
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.data.FeedStatus
import app.melon.feed.interactors.UpdateFeedDetail
import app.melon.feed.ui.state.FeedDetailViewState
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
                    is FeedStatus.Success -> copy(pageItem = it.data)
                    is FeedStatus.Error -> copy(error = it.throwable)
                    else -> copy() // TODO complete logic
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