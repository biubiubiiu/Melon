package app.melon.user.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.melon.data.entities.User
import app.melon.user.data.mapper.RemoteUserToUser
import app.melon.user.data.remote.UserListResponse
import app.melon.util.mappers.toListMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class UserPagingSource constructor(
    private val fetcher: suspend (Int) -> Result<List<UserListResponse>>,
    private val pageSize: Int,
    private val listItemMapper: RemoteUserToUser
) : PagingSource<Int, User>() {

    override fun getRefreshKey(state: PagingState<Int, User>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
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