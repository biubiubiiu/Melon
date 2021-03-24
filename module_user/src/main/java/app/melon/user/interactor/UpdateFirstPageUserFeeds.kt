package app.melon.user.interactor

import app.melon.data.entities.Feed
import app.melon.domain.SuspendingWorkInteractor
import app.melon.user.data.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateFirstPageUserFeeds @Inject constructor(
    private val repository: UserProfileRepository
) : SuspendingWorkInteractor<UpdateFirstPageUserFeeds.Params, List<Feed>>() {

    override suspend fun doWork(params: Params): List<Feed> {
        return withContext(Dispatchers.IO) {
            repository.getFirstPageUserFeeds(params.uid)
        }
    }

    data class Params(val uid: String)
}