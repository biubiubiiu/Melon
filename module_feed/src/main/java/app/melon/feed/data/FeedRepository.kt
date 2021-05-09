package app.melon.feed.data

import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import app.melon.data.MelonDatabase
import app.melon.data.constants.FeedPageType
import app.melon.data.entities.Feed
import app.melon.data.entities.PoiInfo
import app.melon.data.entities.User
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.data.util.mergeFeed
import app.melon.data.util.mergeUser
import app.melon.feed.data.mapper.RemoteFeedDetailToFeedAndAuthor
import app.melon.feed.data.mapper.RemoteFeedListToFeedAndAuthor
import app.melon.feed.data.mapper.RemoteFeedListToFeedAuthorPair
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@OptIn(ExperimentalPagingApi::class)
class FeedRepository @Inject constructor(
    private val feedApiService: FeedApiService,
    private val database: MelonDatabase,
    private val itemMapper: RemoteFeedListToFeedAndAuthor,
    private val listItemMapper: RemoteFeedListToFeedAuthorPair,
    private val detailItemMapper: RemoteFeedDetailToFeedAndAuthor
) {

    @WorkerThread
    suspend fun getFeedDetail(id: String): FeedStatus<FeedAndAuthor> {
        return runCatching {
            val response = feedApiService.detail(id).getOrThrow()
            val (feed, user) = withContext(Dispatchers.Default) {
                detailItemMapper.map(response)
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
            result!!
        }.fold(
            onSuccess = { data ->
                FeedStatus.Success(data)
            },
            onFailure = { throwable ->
                FeedStatus.Error.Generic(throwable)
            }
        )
    }

    fun getFeedList(timestamp: String, @FeedPageType queryType: Int): Flow<PagingData<FeedAndAuthor>> {
        return Pager(
            config = PAGING_CONFIG,
            remoteMediator = FeedRemoteMediator(
                timestamp,
                queryType,
                feedApiService,
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
                    fetcher = { page -> feedApiService.feedsFromUser(uid, page, PAGING_CONFIG.pageSize) },
                    pageSize = PAGING_CONFIG.pageSize,
                    listItemMapper = itemMapper
                )
            }
        ).flow
    }

    fun getNearbyFeeds(longitude: Double, latitude: Double): Flow<PagingData<FeedAndAuthor>> {
        return Pager(
            config = PAGING_CONFIG,
            pagingSourceFactory = {
                FeedPagingSource(
                    fetcher = { page -> feedApiService.nearby(longitude, latitude, page, PAGING_CONFIG.pageSize) },
                    pageSize = PAGING_CONFIG.pageSize,
                    listItemMapper = itemMapper
                )
            }
        ).flow
    }

    @WorkerThread
    suspend fun postFeed(content: String, images: List<Uri>, location: PoiInfo?): Result<Boolean> {
        return feedApiService.postFeed(content, images.map { it.toString() }, location.toString())
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 15,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}