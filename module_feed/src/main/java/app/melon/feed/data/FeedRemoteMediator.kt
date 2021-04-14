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
import app.melon.feed.data.mapper.RemoteFeedListToFeedAuthorPair
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toException
import app.melon.util.extensions.toResult
import app.melon.util.mappers.toListMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediator constructor(
    private val timestamp: Long,
    @FeedPageType private val queryType: Int,
    private val service: FeedApiService,
    private val database: MelonDatabase,
    private val listItemMapper: RemoteFeedListToFeedAuthorPair
) : RemoteMediator<Int, EntryWithFeedAndAuthor>() {

    private companion object {
        const val STARTING_PAGE_INDEX = 0
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, EntryWithFeedAndAuthor>): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> STARTING_PAGE_INDEX
                // We never need to prepend, since REFRESH will always load the
                // first page in the list. Immediately return, reporting end of pagination.
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val entry = state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                    entry.entry.page + 1
                }
            }

            val apiResponse = withContext(Dispatchers.IO) {
                service.list(timestamp, queryType, page, state.config.pageSize)
                    .executeWithRetry()
                    .toResult()
                    .getOrThrow()
            }
            if (!apiResponse.isSuccess) {
                return MediatorResult.Error(apiResponse.errorMessage.toException())
            }
            val items = withContext(Dispatchers.Default) {
                listItemMapper.toListMapper().invoke(apiResponse.data ?: emptyList())
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
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private fun mergeFeed(local: Feed, remote: Feed) = local.copy(
        id = remote.id,
        authorUid = remote.authorUid,
        content = remote.content,
        photos = remote.photos,
        postTime = remote.postTime,
        replyCount = remote.replyCount,
        favouriteCount = remote.favouriteCount
    )

    private fun mergeUser(local: User, remote: User) = local.copy(
        id = remote.id,
        username = remote.username,
        gender = remote.gender,
        age = remote.age,
        school = remote.school,
        location = remote.location,
        avatarUrl = remote.avatarUrl
    )
}