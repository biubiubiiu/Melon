package app.melon.account.data

import app.melon.account.data.remote.UserDetailResponse
import app.melon.data.dto.BaseApiResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


internal interface AccountApi {

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<AccountResponse>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<AccountResponse>

    @GET("user/logout")
    suspend fun logout(): Response<AccountResponse>

    @GET("user/details")
    suspend fun fetchUserDetail(): Response<BaseApiResponse<UserDetailResponse>>
}