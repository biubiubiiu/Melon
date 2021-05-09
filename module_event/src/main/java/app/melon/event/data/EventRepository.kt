package app.melon.event.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.account.api.UserManager
import app.melon.data.MelonDatabase
import app.melon.data.constants.EventPageType
import app.melon.data.entities.Event
import app.melon.data.entities.User
import app.melon.data.resultentities.EntryWithEventAndOrganiser
import app.melon.data.resultentities.EventAndOrganiser
import app.melon.data.util.mergeEvent
import app.melon.data.util.mergeUser
import app.melon.event.EventApiService
import app.melon.event.data.mapper.RemoteEventListToEventOrganiserPair
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
    private val itemMapper: RemoteEventListToEventOrganiserPair,
    private val userManager: UserManager
) {

    suspend fun getEventDetail(id: String): Result<EventAndOrganiser> {
        return runCatching {
            val response = service.detail(id).getOrThrow()
            val (event, user) = withContext(Dispatchers.Default) {
                itemMapper.map(response)
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
            result!!
        }.fold(
            onSuccess = {
                Result.success(it)
            },
            onFailure = {
                Result.failure(it)
            }
        )
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
        val uid = userManager.user?.id ?: "fake_uid" // TODO check logic
        database.runWithTransaction {
            database.attendEventDao().delete(eventId, uid)
        }
        return syncEventStatus(eventId)
    }

    // TODO sync event status
    suspend fun syncEventStatus(id: String): Event {
        return withContext(Dispatchers.IO) {
            val data = service.detail(id).getOrThrow()
            itemMapper.map(data).first
        }
    }

    suspend fun isJoiningEvent(eventId: String): Boolean {
        val uid = userManager.user?.id ?: "fake_uid" // TODO check logic
        return withContext(Dispatchers.IO) {
            database.attendEventDao().getAttendEventRecord(eventId = eventId, userId = uid) != null
        }
    }

    private companion object {
        val DEFAULT_PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 20,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}