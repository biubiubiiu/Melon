package app.melon.comment.ui

import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ReduxViewModel
import app.melon.comment.interactor.UpdateComment
import app.melon.comment.interactor.UpdateReplyList
import app.melon.comment.ui.state.ReplyListViewState
import app.melon.util.base.ErrorResult
import app.melon.util.base.Success
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch


internal class ReplyListViewModel @AssistedInject constructor(
    @Assisted private val initialState: ReplyListViewState,
    private val updateComment: UpdateComment,
    private val updateReplyList: UpdateReplyList
) : ReduxViewModel<ReplyListViewState>(
    initialState = initialState
) {

    init {
        viewModelScope.launch {
            updateComment.observe().collectAndSetState {
                when (it) {
                    is Success -> copy(viewComment = it.get())
                    is ErrorResult -> copy(error = it.throwable)
                }
            }
        }
        refresh(initialState.id)
    }

    internal val repliesPagingData = updateReplyList.observe()

    private fun refresh(id: String) {
        updateComment(UpdateComment.Params(id))
        updateReplyList(UpdateReplyList.Params(id))
    }

    @AssistedFactory
    internal interface Factory {
        fun create(initialState: ReplyListViewState): ReplyListViewModel
    }
}

internal fun ReplyListViewModel.Factory.create(
    id: String
): ReplyListViewModel {
    val initialState = ReplyListViewState(id = id)
    return create(initialState)
}