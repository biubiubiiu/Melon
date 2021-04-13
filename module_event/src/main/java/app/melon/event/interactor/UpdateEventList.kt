package app.melon.event.interactor

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.base.domain.PagingInteractor
import app.melon.data.entities.Event
import app.melon.event.data.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateEventList @Inject constructor(
    private val repo: EventRepository
) : PagingInteractor<UpdateEventList.Params, Event>() {

    override fun createObservable(params: Params): Flow<PagingData<Event>> {
        return when (params.type) {
            QueryType.NEARBY -> repo.getStream()
            QueryType.JOINING -> repo.getJoiningEvents(PAGING_CONFIG)
            QueryType.ORGANISED -> repo.getOrganisedEvents(PAGING_CONFIG)
        }
    }

    data class Params(
        val type: QueryType,
        override val pagingConfig: PagingConfig = PAGING_CONFIG
    ) : Parameters<Event>

    enum class QueryType { NEARBY, JOINING, ORGANISED }

    companion object {
        private const val PAGE_SIZE = 8
        private val PAGING_CONFIG = PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = 10,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}