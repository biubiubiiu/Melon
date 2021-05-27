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

    /**
     * Requires locale storage
     */
    fun getFeedList(timestamp: String, @FeedPageType queryType: Int): Flow<PagingData<FeedAndAuthor>> {
        return Pager(
            config = DEFAULT_PAGING_CONFIG,
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

    fun getFeedsFromUser(uid: String, isAnonymous: Boolean): Flow<PagingData<FeedAndAuthor>> {
        return Pager(
            config = DEFAULT_PAGING_CONFIG,
            pagingSourceFactory = {
                FeedPagingSource(
                    fetcher = { page ->
                        feedApiService.feedsFromUser(
                            uid,
                            page,
                            DEFAULT_PAGING_CONFIG.pageSize,
                            isAnonymous
                        )
                    },
                    pageSize = DEFAULT_PAGING_CONFIG.pageSize,
                    listItemMapper = itemMapper
                )
            }
        ).flow
    }

    fun getFavorsOfUser(uid: String, config: PagingConfig? = null): Flow<PagingData<FeedAndAuthor>> {
        val pagingConfig = config ?: DEFAULT_PAGING_CONFIG
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                FeedPagingSource(
                    fetcher = { page ->
                        feedApiService.favorsOfUser(uid, page, pagingConfig.pageSize)
                    },
                    pageSize = pagingConfig.pageSize,
                    listItemMapper = itemMapper
                )
            }
        ).flow
    }

    fun getBookmarksOfUser(uid: String, config: PagingConfig? = null): Flow<PagingData<FeedAndAuthor>> {
        val pagingConfig = config ?: DEFAULT_PAGING_CONFIG
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                FeedPagingSource(
                    fetcher = { page ->
                        feedApiService.bookmarksOfUser(uid, page, pagingConfig.pageSize)
                    },
                    pageSize = pagingConfig.pageSize,
                    listItemMapper = itemMapper
                )
            }
        ).flow
    }

    fun getPoiFeeds(id: String, config: PagingConfig? = null): Flow<PagingData<FeedAndAuthor>> {
        val pagingConfig = config ?: DEFAULT_PAGING_CONFIG
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                FeedPagingSource(
                    fetcher = { page ->
                        feedApiService.poiFeeds(id, page, pagingConfig.pageSize)
                    },
                    pageSize = pagingConfig.pageSize,
                    listItemMapper = itemMapper
                )
            }
        ).flow
    }

    @WorkerThread
    suspend fun postFeed(content: String, images: List<Uri>, location: PoiInfo?): Result<Boolean> {
        return feedApiService.postFeed(content, images.map { it.toString() }, location.toString())
    }

    @WorkerThread
    suspend fun addFeedToCollection(id: String): Result<Boolean> {
        val result = feedApiService.collect(id)
        result.onSuccess {
            database.runWithTransaction {
                val feed = database.feedDao().getFeedWithId(id)
                if (feed != null) {
                    val updated = feed.copy(isCollected = feed.isCollected.not())
                    database.feedDao().update(updated)
                }
            }
        }
        return result
    }

    private companion object {
        val DEFAULT_PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 15,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}