package app.melon.comment.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.comment.data.CommentRepository
import app.melon.data.entities.Comment
import app.melon.data.entities.Feed
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateComment @Inject constructor(
    private val repo: CommentRepository
) : SuspendingWorkInteractor<UpdateComment.Params, Result<Comment>>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): Result<Comment> {
        return try {
            loadingState.addLoader()
            repo.getCommentDetail(params.id).also { loadingState.removeLoader() }
        } catch (e: Exception) {
            ErrorResult<Comment>(e).also { loadingState.removeLoader() }
        }
    }

    data class Params(
        val id: String
    )
}