package app.melon.event.data

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
}