package app.melon.domain.interactors

import app.melon.data.fetch
import app.melon.data.repositories.FeedStore
import app.melon.data.repositories.FollowingFeedModule
import app.melon.data.repositories.FollowingFeedStore
import app.melon.domain.Interactor
import app.melon.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateFollowingFeeds @Inject constructor(
    private val followingFeedStore: FollowingFeedStore,
    private val feedStore: FeedStore,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateFollowingFeeds.Params, Boolean>() {

    override suspend fun doWork(params: Params): Boolean {
        return withContext(dispatchers.io) {
            val entries = followingFeedStore.fetch(
                FollowingFeedModule.Params(params.timestamp, params.page, params.pageSize),
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