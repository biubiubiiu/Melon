package app.melon.account.data

import app.melon.account.data.remote.UserDetailResponse
import app.melon.data.dto.BaseApiResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

internal interface AccountApi {

    @Headers("Content-Type: application/json")
    @POST("user/register")
    suspend fun register(
        @Query("username") username: String,
        @Query("password") password: String
        // TODO use @Body jsonBody: JsonObject in project
    ): Response<AccountResponse>

    @Headers("Content-Type: application/json")
    @POST("user/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<AccountResponse>

    @GET("user/logout")
    suspend fun logout(): Response<AccountResponse>

    @GET("user/details")
    suspend fun fetchUserDetail(): Response<BaseApiResponse<UserDetailResponse>>
}