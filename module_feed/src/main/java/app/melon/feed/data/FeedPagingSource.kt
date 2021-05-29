package app.melon.feed.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.data.mapper.RemoteFeedListToFeedAndAuthor
import app.melon.feed.data.remote.FeedListItemResponse
import app.melon.util.mappers.toListMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class FeedPagingSource constructor(
    private val fetcher: suspend (Int) -> Result<List<FeedListItemResponse>>,
    private val pageSize: Int,
    private val listItemMapper: RemoteFeedListToFeedAndAuthor
) : PagingSource<Int, FeedAndAuthor>() {

    override fun getRefreshKey(state: PagingState<Int, FeedAndAuthor>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FeedAndAuthor> {
        val position = params.key ?: 0
        return try {
            val data = withContext(Dispatchers.IO) {
                fetcher.invoke(position).getOrThrow()
            }
            val items = withContext(Dispatchers.Default) {
                listItemMapper.toListMapper().invoke(data)
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