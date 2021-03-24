package app.melon.user

import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.base.framework.ReduxViewModel
import app.melon.user.interactor.UpdateFirstPageUserFeeds
import app.melon.user.interactor.UpdateUserDetail
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    private val updateUserDetail: UpdateUserDetail,
    private val updateFirstPageUserFeeds: UpdateFirstPageUserFeeds
) : ReduxViewModel<UserProfileViewState>(
    initialState = UserProfileViewState()
) {

    private val loadingState = ObservableLoadingCounter()
    private val pendingActions = MutableSharedFlow<UserProfileAction>()

    init {
        viewModelScope.launch {
            loadingState.observable.collectAndSetState { copy(refreshing = it) }
        }
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is UserProfileAction.EnterUserProfile -> fetchUserDetail(action.uid)
                    is UserProfileAction.FetchFirstPageUserFeeds -> fetchUserFeedsFirstPage(action.uid)
                }
            }
        }
        viewModelScope.launch {
            updateUserDetail.observe().collectAndSetState { copy(user = it) }
        }
        viewModelScope.launch {
            updateFirstPageUserFeeds.observe().collectAndSetState { copy(feeds = it) }
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

    fun getUserDetail(uid: String) {
        viewModelScope.launch {
            pendingActions.emit(UserProfileAction.EnterUserProfile(uid))
        }
    }

    fun getUserFeedsFirstPage(uid: String) {
        viewModelScope.launch {
            pendingActions.emit(UserProfileAction.FetchFirstPageUserFeeds(uid))
        }
    }
}