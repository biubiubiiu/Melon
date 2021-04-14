package app.melon.comment.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.comment.data.mapper.RemoteCommentToCommentAndAuthor
import app.melon.data.resultentities.CommentAndAuthor
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import app.melon.util.base.Success
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toException
import app.melon.util.extensions.toResult
import app.melon.util.mappers.toListMapper
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

    suspend fun fetchCommentList(id: String, page: Int, pageSize: Int): List<CommentAndAuthor> {
        return withContext(Dispatchers.IO) {
            service.list(id, page, pageSize)
                .executeWithRetry()
                .toResult {
                    itemMapper.toListMapper().invoke(it.data ?: emptyList())
                }
                .getOrThrow()
        }
    }

    suspend fun getCommentDetail(id: String): Result<CommentAndAuthor> {
        val apiResponse = withContext(Dispatchers.IO) {
            service.detail(id)
                .executeWithRetry()
                .toResult()
                .getOrThrow()
        }
        if (!apiResponse.isSuccess) {
            return ErrorResult(apiResponse.errorMessage.toException())
        }
        val item = withContext(Dispatchers.Default) {
            itemMapper.map(apiResponse.data!!)
        }
        return Success(item)
    }

    fun fetchReplyList(id: String, config: PagingConfig): Flow<PagingData<CommentAndAuthor>> {
        return Pager(
            config = config,
            pagingSourceFactory = {
                CommentPagingSource(id, service, config.pageSize, itemMapper)
            }
        ).flow
    }
}