package app.melon.home.nearby

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.data.entities.Feed
import app.melon.data.entities.User
import app.melon.data.services.FeedApiService
import app.melon.data.services.UserApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NearbyRepository @Inject constructor(
    private val service: UserApiService
) {

    fun getStream(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = INITIAL_LOAD_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NearbyPagingSource(service, PAGE_SIZE)
            }
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val INITIAL_LOAD_SIZE = 20
        private const val PREFETCH_DISTANCE = 3
    }
}