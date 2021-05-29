package app.melon.user.data

import app.melon.base.network.ApiBaseException
import app.melon.base.network.MelonApiService
import app.melon.user.data.remote.UserListResponse
import app.melon.user.data.remote.UpdateAvatarResponse
import app.melon.user.data.remote.UserDetailResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class UserApiService @Inject constructor(
    private val api: UserApi
) : MelonApiService() {

    internal suspend fun nearby(
        longitude: Double,
        latitude: Double,
        page: Int,
        pageSize: Int
    ): Result<List<UserListResponse>> {
        return call {
            api.nearby(longitude, latitude, page, pageSize)
        }
    }

    internal suspend fun detail(
        id: String
    ): Result<UserDetailResponse> {
        return call {
            api.detail(id)
        }
    }

    internal suspend fun followers(
        uid: String,
        page: Int,
        pageSize: Int
    ): Result<List<UserListResponse>> {
        return call {
            api.followers(uid, page, pageSize)
        }
    }

    internal suspend fun followings(
        uid: String,
        page: Int,
        pageSize: Int
    ): Result<List<UserListResponse>> {
        return call {
            api.followings(uid, page, pageSize)
        }
    }

    internal suspend fun updateAvatar(
        file: File
    ): Result<UpdateAvatarResponse> {
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                name = "file",
                filename = file.name,
                body = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            )
            .build()
        return call {
            api.updateAvatar(body.parts)
        }
    }

    // TODO define custom error
    override fun mapApiThrowable(message: String, code: Int): ApiBaseException {
        return super.mapApiThrowable(message, code)
    }
}