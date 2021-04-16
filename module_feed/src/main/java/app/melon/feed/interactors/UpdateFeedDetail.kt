package app.melon.feed.interactors

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.data.FeedRepository
import app.melon.util.base.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UpdateFeedDetail @Inject constructor(
    private val repo: FeedRepository
) : SuspendingWorkInteractor<UpdateFeedDetail.Params, Result<FeedAndAuthor>>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): Result<FeedAndAuthor> {
        loadingState.addLoader()
        return withContext(Dispatchers.IO) {
            repo.getFeedDetail(params.id)
        }.also { loadingState.removeLoader() }
    }

    data class Params(val id: String)
}