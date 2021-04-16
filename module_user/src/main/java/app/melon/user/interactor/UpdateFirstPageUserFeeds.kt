package app.melon.user.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.user.data.UserRepository
import app.melon.util.base.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UpdateFirstPageUserFeeds @Inject constructor(
    private val repository: UserRepository
) : SuspendingWorkInteractor<UpdateFirstPageUserFeeds.Params, Result<List<FeedAndAuthor>>>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): Result<List<FeedAndAuthor>> {
        loadingState.addLoader()
        return withContext(Dispatchers.IO) {
            repository.getFirstPageUserFeeds(params.uid)
        }.also { loadingState.removeLoader() }
    }

    data class Params(val uid: String)
}