package app.melon.comment.interactor

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.base.framework.ObservableLoadingCounter
import app.melon.comment.data.CommentRepository
import app.melon.data.entities.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateComments @Inject constructor(
    private val repo: CommentRepository
) : SuspendingWorkInteractor<UpdateComments.Params, List<Comment>>() {

    private val data = mutableListOf<Comment>()
    val loadingMore = ObservableLoadingCounter()

    override suspend fun doWork(params: Params): List<Comment> {
        loadingMore.addLoader()
        val list = withContext(Dispatchers.IO) {
            repo.fetchCommentList(params.id, params.page, params.pageSize)
        }
        data.addAll(list)
        loadingMore.removeLoader()
        return data
    }

    data class Params(
        val id: String,
        val page: Int,
        val pageSize: Int = 10
    )
}