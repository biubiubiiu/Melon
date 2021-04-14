package app.melon.event.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.data.MelonDatabase
import app.melon.data.constants.EventPageType
import app.melon.data.entities.Event
import app.melon.data.entities.User
import app.melon.data.resultentities.EntryWithEventAndOrganiser
import app.melon.data.resultentities.EventAndOrganiser
import app.melon.event.data.mapper.RemoteEventListToEventOrganiserPair
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import app.melon.util.base.Success
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toException
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@OptIn(ExperimentalPagingApi::class)
class EventRepository @Inject constructor(
    private val service: EventApiService,
    private val database: MelonDatabase,
    private val itemMapper: RemoteEventListToEventOrganiserPair
    // TODO inject UserManager
) {

    suspend fun getEventDetail(id: String): Result<EventAndOrganiser> {
        return try {
            val apiResponse = withContext(Dispatchers.IO) {
                service.detail(id)
                    .executeWithRetry()
                    .toResult()
                    .getOrThrow()
            }
            if (!apiResponse.isSuccess) {
                return ErrorResult(apiResponse.errorMessage.toException())
            }
            val (event, user) = withContext(Dispatchers.Default) {
                itemMapper.map(apiResponse.data!!)
            }
            database.runWithTransaction {
                val localEvent = database.eventDao().getEventWithId(event.id) ?: Event()
                val localUser = database.userDao().getUserWithId(user.id) ?: User()
                database.eventDao().insertOrUpdate(mergeEvent(localEvent, event))
                database.userDao().insertOrUpdate(mergeUser(localUser, user))
            }
            val result = withContext(Dispatchers.IO) {
                database.eventDao().getEventAndOrganiserWithId(id)
            }
            Success(result!!)
        } catch (e: Exception) {
            ErrorResult(e)
        }
    }

    fun getStream(
        @EventPageType queryType: Int,
        customPagingConfig: PagingConfig? = null
    ): Flow<PagingData<EntryWithEventAndOrganiser>> {
        return Pager(
            config = customPagingConfig ?: DEFAULT_PAGING_CONFIG,
            remoteMediator = EventRemoteMediator(
                queryType,
                service,
                database,
                itemMapper
            ),
            pagingSourceFactory = { database.eventEntryDao().eventDataSource(queryType) }
        ).flow
    }

    suspend fun addJoiningEvent(eventId: String): Event {
        val fakeUid = "fake_uid"
        database.runWithTransaction {
            database.attendEventDao().insert(eventId, fakeUid)
        }
        return syncEventStatus(eventId)
    }

    suspend fun removeJoiningEvent(eventId: String): Event {
        val fakeUid = "fake_uid"
        database.runWithTransaction {
            database.attendEventDao().delete(eventId, fakeUid)
        }
        return syncEventStatus(eventId)
    }

    // TODO sync event status
    suspend fun syncEventStatus(id: String): Event {
        return withContext(Dispatchers.IO) {
            service.detail(id)
                .executeWithRetry()
                .toResult {
                    itemMapper.map(it.data!!).first
                }
                .getOrThrow()
        }
    }

    suspend fun isJoiningEvent(eventId: String): Boolean {
        val fakeUid = "fake_uid"
        return withContext(Dispatchers.IO) {
            database.attendEventDao().getAttendEventRecord(eventId = eventId, userId = fakeUid) != null
        }
    }

    private fun mergeEvent(local: Event, remote: Event) = local.copy(
        id = remote.id,
        organiserUid = remote.id,
        title = remote.title,
        content = remote.content,
        startTime = remote.startTime,
        endTime = remote.endTime,
        location = remote.location,
        cost = remote.cost,
        demand = remote.demand
    )

    private fun mergeUser(local: User, remote: User) = local.copy(
        id = remote.id,
        username = remote.username,
        avatarUrl = remote.avatarUrl
    )

    private companion object {
        val DEFAULT_PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 20,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}