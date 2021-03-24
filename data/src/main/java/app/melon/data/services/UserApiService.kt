package app.melon.data.services

import app.melon.data.entities.Feed
import app.melon.data.entities.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService : ApiService {

    @GET("nearby_user")
    fun nearbyUser(
        @Query("longitude") longitude: Float,
        @Query("latitude") latitude: Float,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<List<User>>

    @GET("user/{id}/detail")
    fun userDetail(
        @Path("id") id: String
    ): Call<User>

    @GET("user/{id}/posts")
    fun feedsFromUser(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<List<Feed>>
}