package app.melon.feed.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.base.domain.PaginatedEntryRemoteMediator
import app.melon.data.MelonDatabase
import app.melon.data.constants.FeedType
import app.melon.data.entities.ANExploreFeedEntry
import app.melon.data.entities.ANSchoolFeedEntry
import app.melon.data.entities.ANTrendingFeedEntry
import app.melon.data.entities.Feed
import app.melon.data.resultentities.ANExploreEntryWithFeed
import app.melon.data.resultentities.ANSchoolEntryWithFeed
import app.melon.data.resultentities.ANTrendingEntryWithFeed
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import app.melon.util.base.Success
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class FeedRepository @Inject constructor(
    private val service: FeedApiService,
    private val database: MelonDatabase
) {

    suspend fun getFeedDetail(id: String): Result<Feed> {
        val response = withContext(Dispatchers.IO) {
            service.getFeedDetail(id).execute()
        }
        return when {
            response.isSuccessful && response.body() != null -> Success(response.body()!!)
            else -> ErrorResult(HttpException(response))
        }
    }

    fun getAnonymousSchoolFeed(timestamp: Long): Flow<PagingData<ANSchoolEntryWithFeed>> {
        return Pager(
            config = PAGING_CONFIG,
            remoteMediator = PaginatedEntryRemoteMediator { loadType, page, pageSize ->
                val apiResponse = withContext(Dispatchers.IO) {
                    service.anonymousSchoolFeeds(timestamp, page, pageSize).executeWithRetry().toResult()
                }
                val items = apiResponse.getOrThrow().map { it.copy(type = FeedType.AnonymousSchoolFeed) }
                val entries = items.mapIndexed { index, feed ->
                    ANSchoolFeedEntry(feedId = feed.feedId, page = page, pageOrder = index)
                }
                database.runWithTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.ANSchoolDao().deleteAll()
                        database.feedDao().deleteFeedByType(FeedType.AnonymousSchoolFeed)
                    }
                    database.feedDao().insertAll(items)
                    database.ANSchoolDao().insertAll(entries)
                }
                items.isNullOrEmpty()
            },
            pagingSourceFactory = { database.ANSchoolDao().feedDataSource() }
        ).flow
    }

    fun getAnonymousExploreFeed(timestamp: Long): Flow<PagingData<ANExploreEntryWithFeed>> {
        return Pager(
            config = PAGING_CONFIG,
            remoteMediator = PaginatedEntryRemoteMediator { loadType, page, pageSize ->
                val apiResponse = withContext(Dispatchers.IO) {
                    service.anonymousExploreFeeds(timestamp, page, pageSize).executeWithRetry().toResult()
                }
                val items = apiResponse.getOrThrow().map { it.copy(type = FeedType.AnonymousExploreFeed) }
                val entries = items.mapIndexed { index, feed ->
                    ANExploreFeedEntry(feedId = feed.feedId, page = page, pageOrder = index)
                }
                database.runWithTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.ANExploreDao().deleteAll()
                        database.feedDao().deleteFeedByType(FeedType.AnonymousExploreFeed)
                    }
                    database.feedDao().insertAll(items)
                    database.ANExploreDao().insertAll(entries)
                }
                items.isNullOrEmpty()
            },
            pagingSourceFactory = { database.ANExploreDao().feedDataSource() }
        ).flow
    }

    fun getAnonymousTrendingFeed(timestamp: Long): Flow<PagingData<ANTrendingEntryWithFeed>> {
        return Pager(
            config = PAGING_CONFIG,
            remoteMediator = PaginatedEntryRemoteMediator { loadType, page, pageSize ->
                val apiResponse = withContext(Dispatchers.IO) {
                    service.anonymousTrendingFeeds(timestamp, page, pageSize).executeWithRetry().toResult()
                }
                val items = apiResponse.getOrThrow().map { it.copy(type = FeedType.AnonymousTrendingFeed) }
                val entries = items.mapIndexed { index, feed ->
                    ANTrendingFeedEntry(feedId = feed.feedId, page = page, pageOrder = index)
                }
                database.runWithTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.ANTrendingDao().deleteAll()
                        database.feedDao().deleteFeedByType(FeedType.AnonymousTrendingFeed)
                    }
                    database.feedDao().insertAll(items)
                    database.ANTrendingDao().insertAll(entries)
                }
                items.isNullOrEmpty()
            },
            pagingSourceFactory = { database.ANTrendingDao().feedDataSource() }
        ).flow
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 20,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}