package app.melon.comment.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.comment.data.CommentRepository
import app.melon.data.resultentities.CommentAndAuthor
import app.melon.util.base.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UpdateComment @Inject constructor(
    private val repo: CommentRepository
) : SuspendingWorkInteractor<UpdateComment.Params, Result<CommentAndAuthor>>() {

    val loadingState = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): Result<CommentAndAuthor> {
        loadingState.addLoader()
        return withContext(Dispatchers.IO) {
            repo.getCommentDetail(params.id)
        }.also { loadingState.removeLoader() }
    }

    data class Params(
        val id: String
    )
}