package app.melon.feed.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import app.melon.data.MelonDatabase
import app.melon.data.constants.FeedPageType
import app.melon.data.entities.Feed
import app.melon.data.entities.FeedEntry
import app.melon.data.entities.User
import app.melon.data.resultentities.EntryWithFeedAndAuthor
import app.melon.data.util.mergeFeed
import app.melon.data.util.mergeUser
import app.melon.feed.data.mapper.RemoteFeedListToFeedAuthorPair
import app.melon.util.mappers.toListMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediator(
    private val timestamp: String,
    @FeedPageType private val queryType: Int,
    private val feedApiService: FeedApiService,
    private val database: MelonDatabase,
    private val listItemMapper: RemoteFeedListToFeedAuthorPair
) : RemoteMediator<Int, EntryWithFeedAndAuthor>() {

    private companion object {
        const val STARTING_PAGE_INDEX = 0
    }

//    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.SKIP_INITIAL_REFRESH
//    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EntryWithFeedAndAuthor>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> STARTING_PAGE_INDEX
                // We never need to prepend, since REFRESH will always load the
                // first page in the list. Immediately return, reporting end of pagination.
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val entry =
                        state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                    entry.entry.page + 1
                }
            }

            val response = withContext(Dispatchers.IO) {
                feedApiService.list(timestamp, queryType, page, state.config.pageSize)
                    .getOrThrow()
            }
            val items = withContext(Dispatchers.Default) {
                listItemMapper.toListMapper().invoke(response)
            }
            val entries = items.mapIndexed { index, (feed, _) ->
                FeedEntry(
                    relatedId = feed.id,
                    page = page,
                    pageOrder = index,
                    pageType = queryType
                )
            }
            database.runWithTransaction {
                items.forEach { (feed, user) ->
                    val localFeed = database.feedDao().getFeedWithId(feed.id) ?: Feed()
                    val localUser = database.userDao().getUserWithId(user.id) ?: User()
                    database.feedDao().insertOrUpdate(mergeFeed(localFeed, feed))
                    database.userDao().insertOrUpdate(mergeUser(localUser, user))
                }
                if (loadType == LoadType.REFRESH) {
                    database.feedEntryDao().deleteEntryByType(queryType)
                }
                database.feedEntryDao().insertAll(entries)
            }
            MediatorResult.Success(endOfPaginationReached = items.isEmpty())
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }
}