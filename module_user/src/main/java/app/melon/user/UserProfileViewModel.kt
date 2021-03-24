package app.melon.user

import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.base.framework.ReduxViewModel
import app.melon.user.data.UpdateUserDetail
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    private val updateUserDetail: UpdateUserDetail
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
                }
            }
        }
        viewModelScope.launch {
            updateUserDetail.observe().collectAndSetState { copy(user = it) }
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

    fun getUserDetail(uid: String) {
        viewModelScope.launch {
            pendingActions.emit(UserProfileAction.EnterUserProfile(uid))
        }
    }
}