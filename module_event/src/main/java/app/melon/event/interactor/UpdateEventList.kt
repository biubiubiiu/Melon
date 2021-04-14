package app.melon.event.interactor

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.base.domain.PagingInteractor
import app.melon.data.constants.EventPageType
import app.melon.data.resultentities.EntryWithEventAndOrganiser
import app.melon.event.data.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UpdateEventList @Inject constructor(
    private val repo: EventRepository
) : PagingInteractor<UpdateEventList.Params, EntryWithEventAndOrganiser>() {

    override fun createObservable(params: Params): Flow<PagingData<EntryWithEventAndOrganiser>> {
        return repo.getStream(params.type, customPagingConfig = PAGING_CONFIG)
    }

    data class Params(
        @EventPageType val type: Int,
        override val pagingConfig: PagingConfig = PAGING_CONFIG
    ) : Parameters<EntryWithEventAndOrganiser>

    private companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 8,
            initialLoadSize = 16,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}