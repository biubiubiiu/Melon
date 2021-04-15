package app.melon.user.data

import app.melon.data.MelonDatabase
import app.melon.data.entities.User
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.data.FeedApiService
import app.melon.feed.data.mapper.RemoteFeedListToFeedAndAuthor
import app.melon.user.data.mapper.RemoteUserDetailToUser
import app.melon.util.base.ErrorResult
import app.melon.util.base.Result
import app.melon.util.base.Success
import app.melon.util.extensions.executeWithRetry
import app.melon.util.extensions.toException
import app.melon.util.extensions.toResult
import app.melon.util.mappers.toListMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepository @Inject constructor(
    private val feedService: FeedApiService,
    private val userService: UserApiService,
    private val database: MelonDatabase,
    private val listItemMapper: RemoteFeedListToFeedAndAuthor,
    private val detailItemMapper: RemoteUserDetailToUser
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

    suspend fun getFirstPageUserFeeds(uid: String): List<FeedAndAuthor> {
        val apiResponse = feedService.feedsFromUser(uid, 0, 5)
            .executeWithRetry()
            .toResult()
            .getOrThrow()
        if (!apiResponse.isSuccess) {
            return emptyList()
        }

        // At here, we don't need to store feeds and user information,
        // so we just return what we got from remote
        return withContext(Dispatchers.Default) {
            listItemMapper.toListMapper().invoke(apiResponse.data ?: emptyList())
        }
    }

    private fun mergeUser(local: User, remote: User) = local.copy(
        id = remote.id,
        username = remote.username,
        gender = remote.gender,
        age = remote.age,
        school = remote.school,
        location = remote.location,
        description = remote.description,
        avatarUrl = remote.avatarUrl,
        backgroundUrl = remote.backgroundUrl,
        followerCount = remote.followerCount,
        followingCount = remote.followingCount
    )
}