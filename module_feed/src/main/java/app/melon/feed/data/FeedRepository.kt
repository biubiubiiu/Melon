package app.melon.feed.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import app.melon.data.MelonDatabase
import app.melon.data.constants.FeedPageType
import app.melon.data.entities.Feed
import app.melon.data.entities.User
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.data.util.mergeFeed
import app.melon.data.util.mergeUser
import app.melon.feed.data.mapper.RemoteFeedDetailToFeedAndAuthor
import app.melon.feed.data.mapper.RemoteFeedListToFeedAndAuthor
import app.melon.feed.data.mapper.RemoteFeedListToFeedAuthorPair
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import app.melon.util.base.Success
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toException
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@OptIn(ExperimentalPagingApi::class)
class FeedRepository @Inject constructor(
    private val service: FeedApiService,
    private val database: MelonDatabase,
    private val itemMapper: RemoteFeedListToFeedAndAuthor,
    private val listItemMapper: RemoteFeedListToFeedAuthorPair,
    private val detailItemMapper: RemoteFeedDetailToFeedAndAuthor
) {

    // TODO too long
    suspend fun getFeedDetail(id: String): Result<FeedAndAuthor> {
        return try {
            val apiResponse = withContext(Dispatchers.IO) {
                service.detail(id)
                    .executeWithRetry()
                    .toResult()
                    .getOrThrow()
            }
            if (!apiResponse.isSuccess) {
                return ErrorResult(apiResponse.errorMessage.toException())
            }
            val (feed, user) = withContext(Dispatchers.Default) {
                detailItemMapper.map(apiResponse.data!!)
            }
            database.runWithTransaction {
                val localFeed = database.feedDao().getFeedWithId(feed.id) ?: Feed()
                val localUser = database.userDao().getUserWithId(user.id) ?: User()
                database.feedDao().insertOrUpdate(mergeFeed(localFeed, feed))
                database.userDao().insertOrUpdate(mergeUser(localUser, user))
            }
            val result = withContext(Dispatchers.IO) {
                database.feedDao().getFeedAndAuthorWithId(id)
            }
            Success(result!!)
        } catch (e: Exception) {
            ErrorResult(e)
        }
    }

    fun getFeedList(timestamp: Long, @FeedPageType queryType: Int): Flow<PagingData<FeedAndAuthor>> {
        return Pager(
            config = PAGING_CONFIG,
            remoteMediator = FeedRemoteMediator(
                timestamp,
                queryType,
                service,
                database,
                listItemMapper
            ),
            pagingSourceFactory = { database.feedEntryDao().feedDataSource(queryType) }
        ).flow.map {
            it.map { entry ->
                entry.compoundItem
            }
        }
    }

    fun getFeedsFromUser(uid: String): Flow<PagingData<FeedAndAuthor>> {
        return Pager(
            config = PAGING_CONFIG,
            pagingSourceFactory = {
                FeedPagingSource(
                    fetcher = { page -> service.feedsFromUser(uid, page, PAGING_CONFIG.pageSize) },
                    pageSize = PAGING_CONFIG.pageSize,
                    listItemMapper = itemMapper)
            }
        ).flow
    }

    fun getNearbyFeeds(longitude: Double, latitude: Double): Flow<PagingData<FeedAndAuthor>> {
        return Pager(
            config = PAGING_CONFIG,
            pagingSourceFactory = {
                FeedPagingSource(
                    fetcher = { page -> service.nearby(longitude, latitude, page, PAGING_CONFIG.pageSize) },
                    pageSize = PAGING_CONFIG.pageSize,
                    listItemMapper = itemMapper)
            }
        ).flow
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 20,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}