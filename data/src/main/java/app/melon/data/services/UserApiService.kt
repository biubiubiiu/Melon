package app.melon.data.services

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

    @GET("user/detail/{id}")
    fun userDetail(
        @Path("id") id: String
    ): Call<User>
}