package app.melon.comment.interactor

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.base.domain.PagingInteractor
import app.melon.comment.data.CommentRepository
import app.melon.data.resultentities.CommentAndAuthor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UpdateCommentList @Inject constructor(
    private val repo: CommentRepository
) : PagingInteractor<UpdateCommentList.Params, CommentAndAuthor>() {

    override fun createObservable(params: Params): Flow<PagingData<CommentAndAuthor>> {
        return repo.fetchCommentList(params.id, params.pagingConfig)
    }

    data class Params(
        val id: String,
        override val pagingConfig: PagingConfig = PAGING_CONFIG
    ) : Parameters<CommentAndAuthor>

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