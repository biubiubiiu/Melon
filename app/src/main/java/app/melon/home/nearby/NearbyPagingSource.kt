package app.melon.home.nearby

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.melon.data.entities.User
import app.melon.data.services.UserApiService
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NearbyPagingSource constructor(
    private val service: UserApiService,
    private val pageSize: Int
) : PagingSource<Int, User>() {
    override fun getRefreshKey(state: PagingState<Int, User>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val position = params.key ?: 0
        return try {
            val response = withContext(Dispatchers.IO) {
                service.nearbyUser(1f, 1f, position, params.loadSize).executeWithRetry().toResult()
            }
            val users = response.get() ?: emptyList()
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