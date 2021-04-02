package app.melon.user.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.data.entities.Feed
import app.melon.data.entities.User
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val service: UserApiService
) {

    suspend fun getUserDetail(uid: String): User {
        return service.userDetail(uid).executeWithRetry().toResult().getOrThrow()
    }

    suspend fun getFirstPageUserFeeds(uid: String): List<Feed> {
        return service.feedsFromUser(uid, 0, 5).executeWithRetry().toResult().getOrThrow()
    }

    fun getFeedsFromUser(uid: String, pagingConfig: PagingConfig): Flow<PagingData<Feed>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                UserFeedsPagingSource(uid, service, pagingConfig.pageSize)
            }
        ).flow
    }
}