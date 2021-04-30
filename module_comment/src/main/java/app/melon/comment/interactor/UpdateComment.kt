package app.melon.comment.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.comment.data.CommentRepository
import app.melon.data.resultentities.CommentAndAuthor
import app.melon.util.base.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


internal class UpdateComment @Inject constructor(
    private val repo: CommentRepository
) : SuspendingWorkInteractor<UpdateComment.Params, Result<CommentAndAuthor>>() {

    override suspend fun doWork(params: Params): Result<CommentAndAuthor> {
        return withContext(Dispatchers.IO) {
            repo.getCommentDetail(params.id)
        }
    }

    internal data class Params(
        val id: String
    )
}