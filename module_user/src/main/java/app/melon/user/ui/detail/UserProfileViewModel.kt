package app.melon.user.ui.detail

import android.view.View
import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ReduxViewModel
import app.melon.base.framework.SingleEvent
import app.melon.user.data.UserStatus
import app.melon.user.interactor.UpdateFirstPageUserFeeds
import app.melon.user.interactor.UpdateUserDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


internal class UserProfileViewModel @AssistedInject constructor(
    @Assisted private val initialState: UserProfileViewState,
    private val updateUserDetail: UpdateUserDetail,
    private val updateFirstPageUserFeeds: UpdateFirstPageUserFeeds
) : ReduxViewModel<UserProfileViewState>(initialState) {

    init {
        viewModelScope.launch {
            combine(
                updateUserDetail.loadingState.observable,
                updateFirstPageUserFeeds.loadingState.observable
            ) { firstLoading, secondLoading ->
                firstLoading || secondLoading
            }.collectAndSetState {
                copy(refreshing = it)
            }
        }
        viewModelScope.launch {
            updateUserDetail.observe().collectAndSetState {
                when (it) {
                    is UserStatus.Success -> copy(user = it.data)
                    is UserStatus.Error -> copy(error = it.throwable)
                }
            }
        }
        viewModelScope.launch {
            updateFirstPageUserFeeds.observe().collectAndSetState { result ->
                result.fold(
                    onSuccess = { copy(pageItems = it) },
                    onFailure = { copy(error = it) }
                )
            }
        }
        selectSubscribeDistinct(UserProfileViewState::uid) {
            fetchUserDetail(it)
            fetchUserFeedsFirstPage(it)
        }
    }

    private fun fetchUserDetail(uid: String) {
        viewModelScope.launch {
            updateUserDetail(UpdateUserDetail.Params(uid))
        }
    }

    private fun fetchUserFeedsFirstPage(uid: String) {
        viewModelScope.launch {
            updateFirstPageUserFeeds(UpdateFirstPageUserFeeds.Params(uid))
        }
    }

    internal fun viewMorePosts(view: View) {
        viewModelScope.launchSetState {
            copy(enterMorePosts = SingleEvent(view))
        }
    }

    internal fun toggleFollowState() {
        viewModelScope.launch {
            // TODO update follow state
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: UserProfileViewState): UserProfileViewModel
    }
}

internal fun UserProfileViewModel.Factory.create(
    id: String
): UserProfileViewModel {
    val initialState = UserProfileViewState(uid = id)
    return create(initialState)
}