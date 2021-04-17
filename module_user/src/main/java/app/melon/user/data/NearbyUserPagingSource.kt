package app.melon.user.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.melon.data.entities.User
import app.melon.user.data.mapper.RemoteNearbyUserToUser
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toException
import app.melon.util.extensions.toResult
import app.melon.util.mappers.toListMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class NearbyUserPagingSource constructor(
    private val longitude: Double,
    private val latitude: Double,
    private val service: UserApiService,
    private val pageSize: Int,
    private val listItemMapper: RemoteNearbyUserToUser
) : PagingSource<Int, User>() {
    override fun getRefreshKey(state: PagingState<Int, User>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val loadKey = params.key ?: 0
        return try {
            val response = withContext(Dispatchers.IO) {
                service.nearby(longitude, latitude, loadKey, params.loadSize)
                    .executeWithRetry()
                    .toResult()
                    .getOrThrow()
            }
            if (!response.isSuccess) {
                return LoadResult.Error(response.errorMessage.toException())
            }
            val users = withContext(Dispatchers.IO) {
                listItemMapper.toListMapper().invoke(response.data ?: emptyList())
            }
            val nextKey = if (users.isEmpty()) {
                null
            } else {
                loadKey + (params.loadSize / pageSize)
            }
            LoadResult.Page(
                data = users,
                prevKey = if (loadKey == 0) null else loadKey - 1,
                nextKey = nextKey
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }
}