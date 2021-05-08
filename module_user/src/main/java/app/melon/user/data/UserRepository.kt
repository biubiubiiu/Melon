package app.melon.user.data

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.melon.data.MelonDatabase
import app.melon.data.entities.User
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.data.util.mergeUser
import app.melon.feed.data.FeedApiService
import app.melon.feed.data.mapper.RemoteFeedListToFeedAndAuthor
import app.melon.user.data.mapper.RemoteNearbyUserToUser
import app.melon.user.data.mapper.RemoteUserDetailToUser
import app.melon.util.mappers.toListMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlin.Result
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class UserRepository @Inject constructor(
    private val feedApiService: FeedApiService,
    private val userApiService: UserApiService,
    private val database: MelonDatabase,
    private val listItemMapper: RemoteFeedListToFeedAndAuthor,
    private val detailItemMapper: RemoteUserDetailToUser,
    private val userListItemMapper: RemoteNearbyUserToUser
) {

    @WorkerThread
    internal suspend fun getUserDetail(uid: String): UserStatus<User> {
        return runCatching {
            val response = userApiService.detail(uid).getOrThrow()
            val user = withContext(Dispatchers.Default) {
                detailItemMapper.map(response)
            }
            val result = database.runWithTransaction {
                val localUser = database.userDao().getUserWithId(user.id) ?: User()
                database.userDao().insertOrUpdate(mergeUser(localUser, user))
                database.userDao().getUserWithId(uid)
            }
            result!!
        }.fold(
            onSuccess = { data ->
                UserStatus.Success(data)
            },
            onFailure = { throwable ->
                UserStatus.Error.Generic(throwable)
            }
        )
    }

    internal fun getNearbyUser(longitude: Double, latitude: Double): Flow<PagingData<User>> {
        return Pager(
            config = PAGING_CONFIG,
            pagingSourceFactory = {
                NearbyUserPagingSource(
                    longitude,
                    latitude,
                    userApiService,
                    PAGING_CONFIG.pageSize,
                    userListItemMapper
                )
            }
        ).flow
    }

    @WorkerThread
    internal suspend fun getFirstPageUserFeeds(uid: String): Result<List<FeedAndAuthor>> {
        return feedApiService.feedsFromUser(uid, 0, 5).fold(
            onSuccess = {
                // At here, we don't need to store feeds and user information,
                // so we just return what we got from remote
                val result = withContext(Dispatchers.Default) {
                    listItemMapper.toListMapper().invoke(it)
                }
                Result.success(result)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    @WorkerThread
    internal suspend fun updateAvatar(file: File): Result<String> {
        return runCatching {
            val response = userApiService.updateAvatar(file).getOrThrow()
            response.avatarUrl!!
        }.fold(
            onSuccess = {
                Result.success(it)
            }, onFailure = {
                Result.failure(it)
            }
        )
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 15,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}