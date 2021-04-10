package app.melon.comment.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.melon.data.entities.Comment
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommentPagingSource constructor(
    private val id: String,
    private val service: CommentApiService,
    private val pageSize: Int
) : PagingSource<Int, Comment>() {
    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val position = params.key ?: 0
        return try {
            val response = withContext(Dispatchers.IO) {
                service.reply(id, position, pageSize).executeWithRetry().toResult()
            }
            val replies = response.get() ?: emptyList()
            val nextKey = if (replies.isEmpty()) {
                null
            } else {
                position + (params.loadSize / pageSize)
            }
            LoadResult.Page(
                data = replies,
                prevKey = if (position == 0) null else position - 1,
                nextKey = nextKey
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }
}