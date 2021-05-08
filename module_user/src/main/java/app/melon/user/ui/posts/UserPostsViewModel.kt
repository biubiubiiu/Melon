package app.melon.user.ui.posts

import app.melon.base.framework.ReduxViewModel
import app.melon.user.interactor.UpdateUserFeeds
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

internal class UserPostsViewModel @AssistedInject constructor(
    @Assisted private val initialState: UserFeedsViewState,
    private val updateUserFeeds: UpdateUserFeeds
) : ReduxViewModel<UserFeedsViewState>(initialState) {

    internal val pagingData = updateUserFeeds.observe()

    init {
        selectSubscribeDistinct(UserFeedsViewState::uid) {
            updateUserFeeds(UpdateUserFeeds.Params(it))
        }
    }

    @AssistedFactory
    internal interface Factory {
        fun create(initialState: UserFeedsViewState): UserPostsViewModel
    }
}

internal fun UserPostsViewModel.Factory.create(
    id: String
): UserPostsViewModel {
    val initialState = UserFeedsViewState(uid = id)
    return create(initialState)
}