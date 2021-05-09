package app.melon.comment.data

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.comment.data.mapper.RemoteCommentToCommentAndAuthor
import app.melon.data.resultentities.CommentAndAuthor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CommentRepository @Inject constructor(
    private val service: CommentApiService,
    private val itemMapper: RemoteCommentToCommentAndAuthor
) {

    internal fun fetchCommentList(id: String, config: PagingConfig): Flow<PagingData<CommentAndAuthor>> {
        return Pager(
            config = config,
            pagingSourceFactory = {
                CommentReplyPagingSource(id, service, config.pageSize, itemMapper)
            }
        ).flow
    }

    internal fun fetchReplyList(id: String, config: PagingConfig): Flow<PagingData<CommentAndAuthor>> {
        return Pager(
            config = config,
            pagingSourceFactory = {
                CommentReplyPagingSource(id, service, config.pageSize, itemMapper, fetchComments = false)
            }
        ).flow
    }

    @WorkerThread
    internal suspend fun getCommentDetail(id: String): CommentStatus<CommentAndAuthor> {
        return runCatching {
            val response = service.detail(id).getOrThrow()
            val item = withContext(Dispatchers.Default) {
                itemMapper.map(response)
            }
            item
        }.fold(
            onSuccess = {
                CommentStatus.Success(it)
            },
            onFailure = {
                CommentStatus.Error.Generic(it)
            }
        )
    }
}