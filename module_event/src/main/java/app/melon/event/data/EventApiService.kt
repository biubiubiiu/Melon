package app.melon.event.data

import androidx.annotation.StringDef
import app.melon.data.entities.Event
import app.melon.data.services.ApiService
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
    ): Call<List<Event>>

    @GET("events")
    fun events(
        @Query("type") @EventQueryType type: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<List<Event>>

    @GET("event_detail")
    fun detail(
        @Query("id") id: String
    ): Call<Event>


}

const val ORGANISED = "organised"
const val JOINING = "joining"

@StringDef(ORGANISED, JOINING)
annotation class EventQueryType