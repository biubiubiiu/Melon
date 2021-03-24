package app.melon.user.data

import app.melon.data.entities.User
import app.melon.data.services.UserApiService
import app.melon.extensions.executeWithRetry
import app.melon.extensions.toResult
import javax.inject.Inject

class UserProfileRepository @Inject constructor(
    private val service: UserApiService
) {

    suspend fun getUserDetail(uid: String): User {
        return service.userDetail(uid).executeWithRetry().toResult().getOrThrow()
    }
}