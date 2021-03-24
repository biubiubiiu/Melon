package app.melon.user.data

import app.melon.data.entities.User
import app.melon.domain.SuspendingWorkInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateUserDetail @Inject constructor(
    private val repository: UserProfileRepository
) : SuspendingWorkInteractor<UpdateUserDetail.Params, User>() {

    override suspend fun doWork(params: Params): User {
        return withContext(Dispatchers.IO) {
            repository.getUserDetail(params.uid)
        }
    }

    data class Params(val uid: String)
}