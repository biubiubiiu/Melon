package app.melon.base.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import app.melon.data.PaginatedEntry
import app.melon.data.resultentities.EntryWithFeed

/**
 * A [RemoteMediator] which works on [PaginatedEntry] entities. [fetchAndStoreData] will be called with the
 * next page to load.
 *
 * source: https://github.com/chrisbanes/tivi
 */
@OptIn(ExperimentalPagingApi::class)
open class PaginatedEntryRemoteMediator<LI, ET>(
    private val fetchAndStoreData: suspend (LoadType, Int, Int) -> Boolean
) : RemoteMediator<Int, LI>() where ET : PaginatedEntry, LI : EntryWithFeed<ET> {

    companion object {
        const val DEFAULT_STARTING_PAGE_INDEX = 0
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LI>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> DEFAULT_STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true) // no-op
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                lastItem.entry.page + 1
            }
        }
        return try {
            val endOfPaginationReached = fetchAndStoreData(loadType, page, state.config.pageSize)
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }
}
