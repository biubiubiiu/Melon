package app.melon.comment.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.data.entities.Comment
import app.melon.util.base.Result
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val service: CommentApiService
) {

    suspend fun fetchCommentList(id: String, page: Int, pageSize: Int): List<Comment> {
        return withContext(Dispatchers.IO) {
            service.list(id, page, pageSize).execute().toResult().getOrThrow()
        }
    }

    suspend fun getCommentDetail(id: String): Result<Comment> {
        return withContext(Dispatchers.IO) {
            service.detail(id).execute().toResult()
        }
    }

    fun fetchReplyList(id: String, config: PagingConfig): Flow<PagingData<Comment>> {
        return Pager(
            config = config,
            pagingSourceFactory = {
                CommentPagingSource(id, service, config.pageSize)
            }
        ).flow
    }
}