package app.melon.feed.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.data.FeedApiService
import app.melon.feed.data.mapper.RemoteFeedListToFeedAndAuthor
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toException
import app.melon.util.extensions.toResult
import app.melon.util.mappers.toListMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class FeedPagingSource constructor(
    private val uid: String,
    private val service: FeedApiService,
    private val pageSize: Int,
    private val listItemMapper: RemoteFeedListToFeedAndAuthor
) : PagingSource<Int, FeedAndAuthor>() {
    override fun getRefreshKey(state: PagingState<Int, FeedAndAuthor>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FeedAndAuthor> {
        val position = params.key ?: 0
        return try {
            val apiResponse = withContext(Dispatchers.IO) {
                service.feedsFromUser(uid, position, pageSize)
                    .executeWithRetry()
                    .toResult()
                    .getOrThrow()
            }
            if (!apiResponse.isSuccess) {
                return LoadResult.Error(apiResponse.errorMessage.toException())
            }
            val items = withContext(Dispatchers.Default) {
                listItemMapper.toListMapper().invoke(apiResponse.data ?: emptyList())
            }
            val nextKey = if (items.isEmpty()) {
                null
            } else {
                position + (params.loadSize / pageSize)
            }
            LoadResult.Page(
                data = items,
                prevKey = if (position == 0) null else position - 1,
                nextKey = nextKey
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }
}