package app.melon.user.data

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
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import app.melon.util.base.Success
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toException
import app.melon.util.extensions.toResult
import app.melon.util.mappers.toListMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepository @Inject constructor(
    private val feedService: FeedApiService,
    private val userService: UserApiService,
    private val database: MelonDatabase,
    private val listItemMapper: RemoteFeedListToFeedAndAuthor,
    private val detailItemMapper: RemoteUserDetailToUser,
    private val userListItemMapper: RemoteNearbyUserToUser
) {

    suspend fun getUserDetail(uid: String): Result<User> {
        return try {
            val apiResponse = withContext(Dispatchers.IO) {
                userService.detail(uid)
                    .executeWithRetry()
                    .toResult()
                    .getOrThrow()
            }
            if (!apiResponse.isSuccess) {
                return ErrorResult(apiResponse.errorMessage.toException())
            }
            val user = withContext(Dispatchers.Default) {
                detailItemMapper.map(apiResponse.data!!)
            }
            val result = database.runWithTransaction {
                val localUser = database.userDao().getUserWithId(user.id) ?: User()
                database.userDao().insertOrUpdate(mergeUser(localUser, user))
                database.userDao().getUserWithId(uid)
            }
            Success(result!!)
        } catch (e: Exception) {
            ErrorResult(e)
        }
    }

    fun getNearbyUser(longitude: Double, latitude: Double): Flow<PagingData<User>> {
        return Pager(
            config = PAGING_CONFIG,
            pagingSourceFactory = {
                NearbyUserPagingSource(
                    longitude,
                    latitude,
                    userService,
                    PAGING_CONFIG.pageSize,
                    userListItemMapper
                )
            }
        ).flow
    }

    suspend fun getFirstPageUserFeeds(uid: String): Result<List<FeedAndAuthor>> {
        return try {
            val apiResponse = feedService.feedsFromUser(uid, 0, 5)
                .executeWithRetry()
                .toResult()
                .getOrThrow()
            if (!apiResponse.isSuccess) {
                return ErrorResult(apiResponse.errorMessage.toException())
            }
            // At here, we don't need to store feeds and user information,
            // so we just return what we got from remote
            val result = withContext(Dispatchers.Default) {
                listItemMapper.toListMapper().invoke(apiResponse.data ?: emptyList())
            }
            return Success(result)
        } catch (e: Exception) {
            ErrorResult(e)
        }
    }

    suspend fun updateAvatar(file: File): Result<String> {
        return try {
            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    name = "file",
                    filename = file.name,
                    body = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
                )
                .build()
            val apiResponse = UserApiService.testInstance()
                .updateAvatar(body.parts)
                .executeWithRetry()
                .toResult()
                .getOrThrow()
            if (!apiResponse.isSuccess) {
                return ErrorResult(apiResponse.errorMessage.toException())
            }
            Success(apiResponse.data!!.avatarUrl!!)
        } catch (e: Exception) {
            ErrorResult(e)
        }
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 20,
            prefetchDistance = 3,
            enablePlaceholders = false
        )
    }
}