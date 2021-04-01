package app.melon.user.interactor

import app.melon.data.entities.Feed
import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.user.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateFirstPageUserFeeds @Inject constructor(
    private val repository: UserRepository
) : SuspendingWorkInteractor<UpdateFirstPageUserFeeds.Params, List<Feed>>() {

    override suspend fun doWork(params: Params): List<Feed> {
        return withContext(Dispatchers.IO) {
            repository.getFirstPageUserFeeds(params.uid)
        }
    }

    data class Params(val uid: String)
}