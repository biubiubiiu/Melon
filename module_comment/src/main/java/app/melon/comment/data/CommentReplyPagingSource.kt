package app.melon.comment.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.melon.comment.data.mapper.RemoteCommentToCommentAndAuthor
import app.melon.data.resultentities.CommentAndAuthor
import app.melon.util.mappers.toListMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal class CommentReplyPagingSource(
    private val id: String, // feed id or comment id
    private val commentApiService: CommentApiService,
    private val pageSize: Int,
    private val listItemMapper: RemoteCommentToCommentAndAuthor,
    private val fetchComments: Boolean = true
) : PagingSource<Int, CommentAndAuthor>() {
    override fun getRefreshKey(state: PagingState<Int, CommentAndAuthor>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentAndAuthor> {
        val position = params.key ?: 0
        return try {
            val data = withContext(Dispatchers.IO) {
                val result = if (fetchComments) {
                    commentApiService.list(id, position, pageSize)
                } else {
                    commentApiService.reply(id, position, pageSize)
                }
                result.getOrThrow()
            }
            val replies = withContext(Dispatchers.Default) {
                listItemMapper.toListMapper().invoke(data)
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