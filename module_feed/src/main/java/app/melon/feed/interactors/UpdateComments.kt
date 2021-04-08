package app.melon.feed.interactors

import app.melon.base.domain.SuspendingWorkInteractor
import app.melon.data.entities.Comment
import app.melon.feed.data.CommentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateComments @Inject constructor(
    private val repo: CommentRepository
) : SuspendingWorkInteractor<UpdateComments.Params, List<Comment>>() {

    private val data = mutableListOf<Comment>()

    override suspend fun doWork(params: Params): List<Comment> {
        val list = withContext(Dispatchers.IO) {
            repo.fetchCommentList(params.id, params.page, params.pageSize)
        }
        data.addAll(list)
        return data
    }

    data class Params(
        val id: String,
        val page: Int,
        val pageSize: Int = 5
    )
}