package app.melon.feed.interactors

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.data.entities.Feed
import app.melon.feed.data.FeedRepository
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import javax.inject.Inject


class UpdateFeedDetail @Inject constructor(
    private val repo: FeedRepository
) : SuspendingWorkInteractor<UpdateFeedDetail.Params, Result<Feed>>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): Result<Feed> {
        return try {
            loadingState.addLoader()
            repo.getFeedDetail(params.id).also { loadingState.removeLoader() }
        } catch (e: Exception) {
            ErrorResult<Feed>(e).also { loadingState.removeLoader() }
        }
    }

    data class Params(val id: String)
}