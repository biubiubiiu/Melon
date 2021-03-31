package app.melon.home.following

import androidx.paging.LoadType
import app.melon.data.MelonDatabase
import app.melon.data.constants.FeedType
import app.melon.data.entities.FollowingFeedEntry
import app.melon.data.resultentities.FollowingEntryWithFeed
import app.melon.feed.data.FeedApiService
import app.melon.base.domain.PaginatedEntryRemoteMediator
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FollowRemoteMediator(
    private val timestamp: Long,
    private val service: FeedApiService,
    private val database: MelonDatabase
) : PaginatedEntryRemoteMediator<FollowingEntryWithFeed, FollowingFeedEntry>(
    fetchAndStoreData = { loadType, page, pageSize ->
        val apiResponse = withContext(Dispatchers.IO) {
            service.fetchFollowingList(timestamp, page, pageSize).executeWithRetry().toResult()
        }
        val items = apiResponse.getOrThrow().map { it.copy(type = FeedType.FollowingFeed) }
        val entries = items.mapIndexed { index, feed ->
            FollowingFeedEntry(feedId = feed.feedId, page = page, pageOrder = index)
        }
        database.runWithTransaction {
            if (loadType == LoadType.REFRESH) {
                database.followingDao().deleteAll()
                database.feedDao().deleteFeedByType(FeedType.FollowingFeed)
            }
            database.feedDao().insertAll(items)
            database.followingDao().insertAll(entries)
        }
        items.isNullOrEmpty()
    }
)