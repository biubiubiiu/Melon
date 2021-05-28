package app.melon.user.data

import app.melon.data.dto.BaseApiResponse
import app.melon.user.data.remote.NearbyUserStruct
import app.melon.user.data.remote.UpdateAvatarResponse
import app.melon.user.data.remote.UserDetailResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


internal interface UserApi {

    @GET("nearby_user")
    suspend fun nearby(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Response<BaseApiResponse<List<NearbyUserStruct>>>

    @GET("user/{id}/detail")
    suspend fun detail(
        @Path("id") id: String
    ): Response<BaseApiResponse<UserDetailResponse>>

    @Multipart
    @POST("update/avatar")
    suspend fun updateAvatar(
        @Part partLis: List<MultipartBody.Part>
    ): Response<BaseApiResponse<UpdateAvatarResponse>>
}