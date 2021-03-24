package app.melon.user

import androidx.lifecycle.viewModelScope
import app.melon.base.framework.ReduxViewModel
import app.melon.user.interactor.UpdateUserFeeds
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserPostsViewModel @Inject constructor(
    private val updateUserFeeds: UpdateUserFeeds
) : ReduxViewModel<UserFeedsViewState>(
    initialState = UserFeedsViewState()
) {

    val pagingData = updateUserFeeds.observe()

    init {
        selectSubscribe(UserFeedsViewState::uid) {
            if (it.isBlank()) return@selectSubscribe // TODO remove this when use assist inject
            updateUserFeeds(UpdateUserFeeds.Params(it))
        }
    }

    fun setUid(id: String) {
        viewModelScope.launch {
            setState { copy(uid = id) }
        }
    }
//    @AssistedFactory
//    internal interface Factory {
//        fun create(initialState: UserFeedsViewState): UserPostsViewModel
//    }
}

//internal fun UserPostsViewModel.Factory.create(
//    id: String
//): UserPostsViewModel {
//    val initialState = UserFeedsViewState(uid = id)
//    return create(initialState)
//}