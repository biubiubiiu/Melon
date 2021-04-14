package app.melon.user.interactor

import app.melon.data.entities.User
import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.user.data.UserRepository
import app.melon.util.base.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UpdateUserDetail @Inject constructor(
    private val repository: UserRepository
) : SuspendingWorkInteractor<UpdateUserDetail.Params, Result<User>>() {

    override suspend fun doWork(params: Params): Result<User> {
        return withContext(Dispatchers.IO) {
            repository.getUserDetail(params.uid)
        }
    }

    data class Params(val uid: String)
}