package app.melon.user.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.melon.data.entities.Feed
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserFeedsPagingSource constructor(
    private val uid: String,
    private val service: UserApiService,
    private val pageSize: Int
) : PagingSource<Int, Feed>() {
    override fun getRefreshKey(state: PagingState<Int, Feed>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Feed> {
        val position = params.key ?: 0
        return try {
            val response = withContext(Dispatchers.IO) {
                service.feedsFromUser(uid, position, pageSize).executeWithRetry().toResult()
            }
            val items = response.get() ?: emptyList()
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