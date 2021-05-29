package app.melon.user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import app.melon.user.api.FollowerUserList
import app.melon.user.api.FollowingUserList
import app.melon.user.api.UserListConfig
import app.melon.user.interactor.UpdateUserList
import kotlinx.coroutines.launch
import javax.inject.Inject


internal class UserListViewModel @Inject constructor(
    private val updateUserList: UpdateUserList
) : ViewModel() {

    internal val pagingData = updateUserList.observe().cachedIn(viewModelScope)

    internal fun refresh(config: UserListConfig) {
        when (config) {
            is FollowingUserList -> {
                viewModelScope.launch {
                    updateUserList(UpdateUserList.Params(config.pageType, config.uid))
                }
            }
            is FollowerUserList -> {
                viewModelScope.launch {
                    updateUserList(UpdateUserList.Params(config.pageType, config.uid))
                }
            }
            else -> {
            } // No-op
        }
    }
}