package app.melon.user.interactor

import app.melon.data.entities.User
import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.user.data.UserRepository
import app.melon.user.data.UserStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


internal class UpdateUserDetail @Inject constructor(
    private val repository: UserRepository
) : SuspendingWorkInteractor<UpdateUserDetail.Params, UserStatus<User>>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): UserStatus<User> {
        loadingState.addLoader()
        return withContext(Dispatchers.IO) {
            repository.getUserDetail(params.uid)
        }.also { loadingState.removeLoader() }
    }

    internal data class Params(val uid: String)
}