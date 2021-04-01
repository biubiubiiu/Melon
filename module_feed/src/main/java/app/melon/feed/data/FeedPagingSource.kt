package app.melon.feed.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.melon.data.entities.Feed

class FeedPagingSource constructor(
    private val service: FeedApiService,
    private val pageSize: Int
) : PagingSource<Int, Feed>() {
    override fun getRefreshKey(state: PagingState<Int, Feed>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Feed> {
        val position = params.key ?: 0
        return try {
//            val response = withContext(Dispatchers.IO) {
//                service.nearbyUser(1f, 1f, position, params.loadSize).executeWithRetry().toResult()
//            }
//            val users = response.get() ?: emptyList()
            val users = emptyList<Feed>()
            val nextKey = if (users.isEmpty()) {
                null
            } else {
                position + (params.loadSize / pageSize)
            }
            LoadResult.Page(
                data = users,
                prevKey = if (position == 0) null else position - 1,
                nextKey = nextKey
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }
}