package app.melon.account.data

import app.melon.account.data.remote.UserDetailResponse
import app.melon.data.dto.BaseApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

internal interface AccountApiService {

    @Headers("Content-Type: application/json")
    @POST("user/register")
    fun register(
        @Query("username") username: String,
        @Query("password") password: String
        // TODO use @Body jsonBody: JsonObject in project
    ): Call<AccountResponse>

    @Headers("Content-Type: application/json")
    @POST("user/login")
    fun login(
        @Query("username") username: String,
        @Query("password") password: String
        // TODO use @Body jsonBody: JsonObject in project
    ): Call<AccountResponse>

    @GET("user/logout")
    fun logout(): Call<AccountResponse>

    @GET("user/details")
    fun fetchUserDetail(): Call<BaseApiResponse<UserDetailResponse>>
}