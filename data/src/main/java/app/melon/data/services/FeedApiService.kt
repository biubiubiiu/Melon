package app.melon.data.services

import app.melon.data.entities.Feed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedApiService : ApiService {

    @GET("home_timeline")
    fun fetchRecommendList(
        @Query("timestamp") time: Long,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<List<Feed>>

    @GET("home_following")
    fun fetchFollowingList(
        @Query("timestamp") time: Long,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<List<Feed>>

    @GET("feed/detail/{id}")
    fun getFeedDetail(
        @Path("id") id: String
    ): Call<Feed>
}