package app.melon.event.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import app.melon.data.daos.OrganisedEventDao
import app.melon.data.entities.OrganisedEvent
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@OptIn(ExperimentalPagingApi::class)
class OrganisedEventRemoteMediator(
    private val service: EventApiService,
    private val eventDao: OrganisedEventDao
) : RemoteMediator<Int, OrganisedEvent>() {

    companion object {
        const val DEFAULT_STARTING_PAGE_INDEX = 0
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, OrganisedEvent>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> DEFAULT_STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true) // no-op
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                lastItem.id.toInt()
            }
        }
        return try {
            val endOfPaginationReached = fetchAndStoreData(loadType, page, state.config.pageSize)
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }

    private suspend fun fetchAndStoreData(loadType: LoadType, page: Int, pageSize: Int): Boolean {
        val apiResponse = withContext(Dispatchers.IO) {
            service.events(ORGANISED, page, pageSize).executeWithRetry().toResult()
        }

        val items = apiResponse.getOrThrow().map { OrganisedEvent.from(it) }
        withContext(Dispatchers.IO) {
            if (loadType == LoadType.REFRESH) {
                eventDao.deleteAll()
            }
            eventDao.insertAll(items)
        }

        return items.isNullOrEmpty()
    }
}