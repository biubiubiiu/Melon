package app.melon.user.ui.detail

import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.base.framework.ReduxViewModel
import app.melon.user.interactor.UpdateFirstPageUserFeeds
import app.melon.user.interactor.UpdateUserDetail
import app.melon.util.base.ErrorResult
import app.melon.util.base.Success
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class UserProfileViewModel @AssistedInject constructor(
    @Assisted private val initialState: UserProfileViewState,
    private val updateUserDetail: UpdateUserDetail,
    private val updateFirstPageUserFeeds: UpdateFirstPageUserFeeds
) : ReduxViewModel<UserProfileViewState>(initialState) {

    private val loadingState = ObservableLoadingCounter()
    private val pendingActions = MutableSharedFlow<UserProfileAction>()

    init {
        viewModelScope.launch {
            loadingState.observable.collectAndSetState { copy(refreshing = it) }
        }
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    // TODO add more actions here
                }
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
            updateFirstPageUserFeeds.observe().collectAndSetState { copy(pageItems = it) }
        }
        selectSubscribeDistinct(UserProfileViewState::uid) {
            fetchUserDetail(it)
            fetchUserFeedsFirstPage(it)
        }
    }

    private fun fetchUserDetail(uid: String) {
        viewModelScope.launch {
            val job = launch {
                loadingState.addLoader()
                updateUserDetail(UpdateUserDetail.Params(uid))
            }
            job.invokeOnCompletion { loadingState.removeLoader() }
            job.join()
        }
    }

    private fun fetchUserFeedsFirstPage(uid: String) {
        viewModelScope.launch {
            val job = launch {
                loadingState.addLoader()
                updateFirstPageUserFeeds(UpdateFirstPageUserFeeds.Params(uid))
            }
            job.invokeOnCompletion { loadingState.removeLoader() }
            job.join()
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