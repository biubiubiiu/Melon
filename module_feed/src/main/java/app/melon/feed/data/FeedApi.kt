package app.melon.feed.data

import app.melon.data.constants.FeedPageType
import app.melon.data.dto.BaseApiResponse
import app.melon.feed.data.remote.FeedDetailResponse
import app.melon.feed.data.remote.FeedListItemResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedApi {

    @GET("feed/list")
    suspend fun list(
        @Query("timestamp") time: String,
        @Query("type") @FeedPageType type: Int,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Response<BaseApiResponse<List<FeedListItemResponse>>>

    @GET("feed/detail/{id}")
    suspend fun detail(
        @Path("id") id: String
    ): Response<BaseApiResponse<FeedDetailResponse>>

    @GET("user/{id}/posts")
    suspend fun feedsFromUser(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Response<BaseApiResponse<List<FeedListItemResponse>>>

    @GET("feed/list/nearby")
    suspend fun nearby(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Response<BaseApiResponse<List<FeedListItemResponse>>>

    @FormUrlEncoded
    @POST("feed/post")
    suspend fun postFeed(
        @Field("content") content: String,
        @Field("images") images: List<String>,
        @Field("location") location: String
    ): Response<BaseApiResponse<Boolean>>

    @FormUrlEncoded
    @POST("feed/collect")
    suspend fun collect(
        @Field("id") id: String
    ): Response<BaseApiResponse<Boolean>>
}