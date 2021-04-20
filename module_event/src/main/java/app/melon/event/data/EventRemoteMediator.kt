package app.melon.event.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import app.melon.data.MelonDatabase
import app.melon.data.constants.EventPageType
import app.melon.data.entities.Event
import app.melon.data.entities.EventEntry
import app.melon.data.entities.User
import app.melon.data.resultentities.EntryWithEventAndOrganiser
import app.melon.data.util.mergeEvent
import app.melon.data.util.mergeUser
import app.melon.event.data.mapper.RemoteEventListToEventOrganiserPair
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toResult
import app.melon.util.mappers.toListMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
internal class EventRemoteMediator(
    @EventPageType private val queryType: Int,
    private val service: EventApiService,
    private val database: MelonDatabase,
    private val listItemMapper: RemoteEventListToEventOrganiserPair
) : RemoteMediator<Int, EntryWithEventAndOrganiser>() {

    private companion object {
        const val STARTING_PAGE_INDEX = 0
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, EntryWithEventAndOrganiser>): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> STARTING_PAGE_INDEX
                // We never need to prepend, since REFRESH will always load the
                // first page in the list. Immediately return, reporting end of pagination.
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val entry = state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                    entry.entry.page + 1
                }
            }

            val apiResponse = withContext(Dispatchers.IO) {
                service.events(queryType, page, state.config.pageSize)
                    .executeWithRetry()
                    .toResult()
                    .getOrThrow()
            }
            val items = withContext(Dispatchers.Default) {
                listItemMapper.toListMapper().invoke(apiResponse.data ?: emptyList())
            }
            val entries = items.mapIndexed { index, (event, _) ->
                EventEntry(
                    relatedId = event.id,
                    page = page,
                    pageOrder = index,
                    pageType = queryType
                )
            }
            database.runWithTransaction {
                items.forEach { (event, user) ->
                    val localEvent = database.eventDao().getEventWithId(event.id) ?: Event()
                    val localUser = database.userDao().getUserWithId(user.id) ?: User()
                    database.eventDao().insertOrUpdate(mergeEvent(localEvent, event))
                    database.userDao().insertOrUpdate(mergeUser(localUser, user))
                }
                if (loadType == LoadType.REFRESH) {
                    database.eventEntryDao().deleteEntryByType(queryType)
                }
                database.eventEntryDao().insertAll(entries)
            }
            MediatorResult.Success(endOfPaginationReached = items.isEmpty())
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }
}