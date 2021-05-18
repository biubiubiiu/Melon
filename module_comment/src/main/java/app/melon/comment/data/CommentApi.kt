package app.melon.comment.data

import app.melon.comment.data.remote.CommentStruct
import app.melon.data.dto.BaseApiResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface CommentApi {

    @GET("comment/{id}/list")
    suspend fun list(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Response<BaseApiResponse<List<CommentStruct>>>

    @GET("comment/{id}/detail")
    suspend fun detail(
        @Path("id") id: String
    ): Response<BaseApiResponse<CommentStruct>>

    @GET("comment/{id}/replies")
    suspend fun reply(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Response<BaseApiResponse<List<CommentStruct>>>

    @FormUrlEncoded
    @POST("comment/post")
    suspend fun postComment(
        @Field("id") feedId: String,
        @Field("content") content: String
    ): Response<BaseApiResponse<Boolean>>

    @FormUrlEncoded
    @POST("comment/reply")
    suspend fun postReply(
        @Field("id") commentId: String,
        @Field("content") content: String
    ): Response<BaseApiResponse<Boolean>>
}