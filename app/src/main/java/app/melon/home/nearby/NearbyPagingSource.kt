package app.melon.home.nearby

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.melon.data.entities.Feed
import app.melon.data.services.FeedApiService
import app.melon.extensions.executeWithRetry
import app.melon.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class NearbyPagingSource constructor(
    private val service: FeedApiService
) : PagingSource<Int, Feed>() {
    override fun getRefreshKey(state: PagingState<Int, Feed>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Feed> {
        val position = params.key ?: 0
        return try {
            val response = withContext(Dispatchers.IO) {
                service.fetchNearbyList(100000, position, params.loadSize).executeWithRetry().toResult()
            }
            val repos = response.get() ?: emptyList()
            val nextKey = if (repos.isEmpty()) {
                null
            } else {
                position + (params.loadSize / 10)
            }
            LoadResult.Page(
                data = repos,
                prevKey = if (position == 0) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}