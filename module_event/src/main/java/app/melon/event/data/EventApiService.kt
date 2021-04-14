package app.melon.event.data

import app.melon.data.constants.EventPageType
import app.melon.data.dto.BaseApiResponse
import app.melon.data.services.ApiService
import app.melon.event.data.remote.EventStruct
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EventApiService : ApiService {

    @GET("events")
    fun events(
        @Query("longitude") longitude: Float,
        @Query("latitude") latitude: Float,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<BaseApiResponse<List<EventStruct>>>

    @GET("events")
    fun events(
        @Query("type") @EventPageType type: Int,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<BaseApiResponse<List<EventStruct>>>

    @GET("event_detail")
    fun detail(
        @Query("id") id: String
    ): Call<BaseApiResponse<EventStruct>>
}