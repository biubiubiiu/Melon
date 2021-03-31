package app.melon.home.recommend

import androidx.paging.LoadType
import app.melon.data.MelonDatabase
import app.melon.data.constants.FeedType
import app.melon.data.entities.RecommendedFeedEntry
import app.melon.data.resultentities.RecommendedEntryWithFeed
import app.melon.feed.data.FeedApiService
import app.melon.base.domain.PaginatedEntryRemoteMediator
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecommendRemoteMediator(
    private val timestamp: Long,
    private val service: FeedApiService,
    private val database: MelonDatabase
) : PaginatedEntryRemoteMediator<RecommendedEntryWithFeed, RecommendedFeedEntry>(
    fetchAndStoreData = { loadType, page, pageSize ->
        val apiResponse = withContext(Dispatchers.IO) {
            service.fetchRecommendList(timestamp, page, pageSize).executeWithRetry().toResult()
        }
        val items = apiResponse.getOrThrow().map { it.copy(type = FeedType.RecommendFeed) }
        val entries = items.mapIndexed { index, feed ->
            RecommendedFeedEntry(feedId = feed.feedId, page = page, pageOrder = index)
        }
        database.runWithTransaction {
            if (loadType == LoadType.REFRESH) {
                database.recommendedDao().deleteAll()
                database.feedDao().deleteFeedByType(FeedType.RecommendFeed)
            }
            database.feedDao().insertAll(items)
            database.recommendedDao().insertAll(entries)
        }
        items.isNullOrEmpty()
    }
)