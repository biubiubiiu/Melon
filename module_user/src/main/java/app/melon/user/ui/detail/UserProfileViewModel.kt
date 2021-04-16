package app.melon.user.ui.detail

import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ReduxViewModel
import app.melon.user.interactor.UpdateFirstPageUserFeeds
import app.melon.user.interactor.UpdateUserDetail
import app.melon.util.base.ErrorResult
import app.melon.util.base.Success
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


class UserProfileViewModel @AssistedInject constructor(
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
                    is Success -> copy(user = it.get())
                    is ErrorResult -> copy(error = it.throwable)
                }
            }
        }
        viewModelScope.launch {
            updateFirstPageUserFeeds.observe().collectAndSetState {
                when (it) {
                    is Success -> copy(pageItems = it.get())
                    is ErrorResult -> copy(error = it.throwable)
                }
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