package app.melon.user.data

import app.melon.data.dto.BaseApiResponse
import app.melon.data.services.ApiService
import app.melon.user.data.remote.NearbyUserStruct
import app.melon.user.data.remote.UserDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService : ApiService {

    @GET("nearby_user")
    fun nearby(
        @Query("longitude") longitude: Float,
        @Query("latitude") latitude: Float,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Call<BaseApiResponse<List<NearbyUserStruct>>>

    @GET("user/{id}/detail")
    fun detail(
        @Path("id") id: String
    ): Call<BaseApiResponse<UserDetailResponse>>
}