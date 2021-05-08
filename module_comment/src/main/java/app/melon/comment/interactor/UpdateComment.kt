package app.melon.comment.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.comment.data.CommentRepository
import app.melon.comment.data.CommentStatus
import app.melon.data.resultentities.CommentAndAuthor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


internal class UpdateComment @Inject constructor(
    private val repo: CommentRepository
) : SuspendingWorkInteractor<UpdateComment.Params, CommentStatus<CommentAndAuthor>>() {

    override suspend fun doWork(params: Params): CommentStatus<CommentAndAuthor> {
        return withContext(Dispatchers.IO) {
            repo.getCommentDetail(params.id)
        }
    }

    internal data class Params(
        val id: String
    )
}