package app.melon.user.data

import app.melon.data.dto.BaseApiResponse
import app.melon.user.data.remote.NearbyUserStruct
import app.melon.user.data.remote.UpdateAvatarResponse
import app.melon.user.data.remote.UserDetailResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


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
    @POST("/user/update/avatar")
    suspend fun updateAvatar(
        @Part partLis: List<MultipartBody.Part>,
        @Header("Authorization") token: String = TEST_TOKEN
    ): Response<BaseApiResponse<UpdateAvatarResponse>>

    companion object {
        private const val TEST_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MSIsImNyZWF0ZWQiOjE2MTg0ODgwMDAxNjgsImV4cCI6MTYxODU3NDQwMH0.S-POYNpeP0cgP-itBltYIb18uJZjmJUQYqs_Vp1mjP52EailKO8_A9V1eauCzN-CCsjqeyh7P8u5YtvZBHJA5w"

        fun testInstance(): UserApi {
            val client = OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
                .baseUrl("http://121.43.141.217:8081/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserApi::class.java)
        }
    }
}