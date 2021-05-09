package app.melon.event.data

import app.melon.data.constants.EventPageType
import app.melon.data.dto.BaseApiResponse
import app.melon.event.data.remote.EventStruct
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EventApi {

    @GET("events")
    suspend fun events(
        @Query("longitude") longitude: Float,
        @Query("latitude") latitude: Float,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Response<BaseApiResponse<List<EventStruct>>>

    @GET("events")
    suspend fun events(
        @Query("type") @EventPageType type: Int,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Response<BaseApiResponse<List<EventStruct>>>

    @GET("event_detail")
    suspend fun detail(
        @Query("id") id: String
    ): Response<BaseApiResponse<EventStruct>>
}