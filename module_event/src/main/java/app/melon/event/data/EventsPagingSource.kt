package app.melon.event.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.melon.data.entities.Event
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventsPagingSource constructor(
    private val service: EventApiService,
    private val pageSize: Int
) : PagingSource<Int, Event>() {

    override fun getRefreshKey(state: PagingState<Int, Event>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        val position = params.key ?: 0
        return try {
            val response = withContext(Dispatchers.IO) {
                service.events(1f, 1f, position, params.loadSize).executeWithRetry().toResult()
            }
            val events = response.get() ?: emptyList()
            val nextKey = if (events.isEmpty()) {
                null
            } else {
                position + (params.loadSize / pageSize)
            }
            LoadResult.Page(
                data = events,
                prevKey = if (position == 0) null else position - 1,
                nextKey = nextKey
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }
}