package app.melon.user.interactor

import app.melon.data.entities.User
import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.user.data.UserRepository
import app.melon.util.base.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UpdateUserDetail @Inject constructor(
    private val repository: UserRepository
) : SuspendingWorkInteractor<UpdateUserDetail.Params, Result<User>>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): Result<User> {
        loadingState.addLoader()
        return withContext(Dispatchers.IO) {
            repository.getUserDetail(params.uid)
        }.also { loadingState.removeLoader() }
    }

    data class Params(val uid: String)
}