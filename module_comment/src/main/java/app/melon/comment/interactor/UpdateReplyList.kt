package app.melon.comment.interactor

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.base.domain.PagingInteractor
import app.melon.comment.data.CommentRepository
import app.melon.data.entities.Comment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateReplyList @Inject constructor(
    private val repo: CommentRepository
) : PagingInteractor<UpdateReplyList.Params, Comment>() {

    override fun createObservable(params: Params): Flow<PagingData<Comment>> {
        return repo.fetchReplyList(params.id, params.pagingConfig)
    }

    data class Params(
        val id: String,
        override val pagingConfig: PagingConfig = PAGING_CONFIG
    ) : PagingInteractor.Parameters<Comment>

    companion object {
        private const val PAGE_SIZE = 8
        private val PAGING_CONFIG = PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = 16,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}