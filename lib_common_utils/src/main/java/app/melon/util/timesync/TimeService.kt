package app.melon.util.timesync

import app.melon.data.dto.BaseApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface TimeService {

    @GET("/time")
    fun serverTime(): Call<BaseApiResponse<TimeResponse>>
}