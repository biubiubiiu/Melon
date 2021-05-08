package app.melon.comment.data

import app.melon.comment.data.remote.CommentStruct
import app.melon.data.dto.BaseApiResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentApiService {

    @GET("comment/{id}/list")
    fun list(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<BaseApiResponse<List<CommentStruct>>>

    @GET("comment/{id}/detail")
    fun detail(
        @Path("id") id: String
    ): Call<BaseApiResponse<CommentStruct>>

    @GET("comment/{id}/replies")
    fun reply(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<BaseApiResponse<List<CommentStruct>>>
}