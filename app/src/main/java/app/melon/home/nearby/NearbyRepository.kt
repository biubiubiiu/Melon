package app.melon.home.nearby

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.data.entities.Feed
import app.melon.data.services.FeedApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NearbyRepository @Inject constructor(
    private val service: FeedApiService
) {

    fun getStream(): Flow<PagingData<Feed>> {
        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 20, prefetchDistance = 3, enablePlaceholders = false),
            pagingSourceFactory = {
                NearbyPagingSource(service)
            }
        ).flow
    }
}