package app.melon.user.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.user.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UpdateFirstPageUserFeeds @Inject constructor(
    private val repository: UserRepository
) : SuspendingWorkInteractor<UpdateFirstPageUserFeeds.Params, List<FeedAndAuthor>>() {

    override suspend fun doWork(params: Params): List<FeedAndAuthor> {
        return withContext(Dispatchers.IO) {
            repository.getFirstPageUserFeeds(params.uid)
        }
    }

    data class Params(val uid: String)
}