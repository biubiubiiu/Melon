package app.melon.comment.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.melon.comment.data.mapper.RemoteCommentToCommentAndAuthor
import app.melon.data.resultentities.CommentAndAuthor
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toException
import app.melon.util.extensions.toResult
import app.melon.util.mappers.toListMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommentPagingSource constructor(
    private val id: String,
    private val service: CommentApiService,
    private val pageSize: Int,
    private val listItemMapper: RemoteCommentToCommentAndAuthor
) : PagingSource<Int, CommentAndAuthor>() {
    override fun getRefreshKey(state: PagingState<Int, CommentAndAuthor>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentAndAuthor> {
        val position = params.key ?: 0
        return try {
            val response = withContext(Dispatchers.IO) {
                service.reply(id, position, pageSize)
                    .executeWithRetry()
                    .toResult()
                    .getOrThrow()
            }
            if (!response.isSuccess) {
                return LoadResult.Error(response.errorMessage.toException())
            }
            val replies = withContext(Dispatchers.Default) {
                listItemMapper.toListMapper().invoke(response.data ?: emptyList())
            }
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