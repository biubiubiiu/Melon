package app.melon.feed.data

import app.melon.data.constants.FeedPageType
import app.melon.data.dto.BaseApiResponse
import app.melon.data.services.ApiService
import app.melon.feed.data.remote.FeedDetailResponse
import app.melon.feed.data.remote.FeedListItemResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedApiService : ApiService {

    @GET("feed/list")
    fun list(
        @Query("timestamp") time: Long,
        @Query("type") @FeedPageType type: Int,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<BaseApiResponse<List<FeedListItemResponse>>>

    @GET("feed/detail/{id}")
    fun detail(
        @Path("id") id: String
    ): Call<BaseApiResponse<FeedDetailResponse>>

    @GET("user/{id}/posts")
    fun feedsFromUser(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<BaseApiResponse<List<FeedListItemResponse>>>

    @GET("feed/list/nearby")
    fun nearby(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<BaseApiResponse<List<FeedListItemResponse>>>
}