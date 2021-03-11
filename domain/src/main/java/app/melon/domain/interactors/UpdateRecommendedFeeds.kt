package app.melon.domain.interactors

import app.melon.data.fetch
import app.melon.data.repositories.FeedStore
import app.melon.data.repositories.RecommendedFeedModule
import app.melon.data.repositories.RecommendedFeedStore
import app.melon.domain.Interactor
import app.melon.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateRecommendedFeeds @Inject constructor(
    private val recommendedFeedStore: RecommendedFeedStore,
    private val feedStore: FeedStore,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateRecommendedFeeds.Params, Boolean>() {

    override suspend fun doWork(params: Params): Boolean {
        return withContext(dispatchers.io) {
            val entries = recommendedFeedStore.fetch(
                RecommendedFeedModule.Params(params.timestamp, params.page, params.pageSize),
                forceFresh = params.forceRefresh
            )
            entries.forEach {
                feedStore.fetch(it.feedId)
            }
            entries.isNullOrEmpty()
        }
    }

    data class Params(
        val page: Int,
        val pageSize: Int,
        val timestamp: Long,
        val forceRefresh: Boolean = false
    )
}