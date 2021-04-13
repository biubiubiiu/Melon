package app.melon.event.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import app.melon.data.daos.EventDao
import app.melon.data.daos.OrganisedEventDao
import app.melon.data.entities.Event
import app.melon.data.entities.JoiningEvent
import app.melon.data.entities.toEvent
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@OptIn(ExperimentalPagingApi::class)
class EventRepository @Inject constructor(
    private val service: EventApiService,
    private val eventDao: EventDao,
    private val organisedEventDao: OrganisedEventDao
) {

    fun getStream(): Flow<PagingData<Event>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = INITIAL_LOAD_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                EventsPagingSource(service, PAGE_SIZE)
            }
        ).flow
    }

    fun getOrganisedEvents(pagingConfig: PagingConfig): Flow<PagingData<Event>> {
        return Pager(
            config = pagingConfig,
            remoteMediator = OrganisedEventRemoteMediator(
                service,
                organisedEventDao
            ),
            pagingSourceFactory = { organisedEventDao.eventsDataSource() }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toEvent()
            }
        }
    }

    fun getJoiningEvents(pagingConfig: PagingConfig): Flow<PagingData<Event>> {
        return Pager(
            config = pagingConfig,
            remoteMediator = JoiningEventRemoteMediator(
                service,
                eventDao
            ),
            pagingSourceFactory = { eventDao.eventsDataSource() }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toEvent()
            }
        }
    }

    suspend fun getEventDetail(id: String): Event {
        return withContext(Dispatchers.IO) {
            service.detail(id).execute().toResult().getOrThrow()
        }
    }

    suspend fun addJoiningEvent(event: Event): Event {
        withContext(Dispatchers.IO) {
            eventDao.insert(JoiningEvent.from(event))
        }
        return syncEventStatus(event.id)
    }

    suspend fun removeJoiningEvent(event: Event): Event {
        withContext(Dispatchers.IO) {
            eventDao.deleteEventWithId(event.id)
        }
        return syncEventStatus(event.id)
    }

    suspend fun syncEventStatus(id: String): Event {
        return withContext(Dispatchers.IO) { // TODO
            service.detail(id).execute().toResult().getOrThrow()
        }
    }

    suspend fun isJoiningEvent(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            eventDao.getEventWithId(id) != null
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val INITIAL_LOAD_SIZE = 20
        private const val PREFETCH_DISTANCE = 3
    }
}