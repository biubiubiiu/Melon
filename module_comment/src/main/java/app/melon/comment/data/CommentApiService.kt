package app.melon.comment.data

import app.melon.data.entities.Comment
import app.melon.data.services.ApiService
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentApiService : ApiService {

    @GET("comment/{id}/list")
    fun list(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<List<Comment>>

    @GET("comment/{id}/detail")
    fun detail(
        @Path("id") id: String
    ): Call<Comment>

    @GET("comment/{id}/replies")
    fun reply(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<List<Comment>>
}