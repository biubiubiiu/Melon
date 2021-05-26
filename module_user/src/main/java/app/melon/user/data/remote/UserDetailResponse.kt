package app.melon.user.data.remote

import app.melon.data.remote.LocationStruct
import com.google.gson.annotations.SerializedName

internal data class UserDetailResponse(
    @SerializedName("id") val id: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("customId") val customId: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("backgroundUrl") val backgroundUrl: String? = null,
    @SerializedName("school") val school: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("photos") val photos: List<String>? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("followers") val followerCount: Long? = null,
    @SerializedName("following") val followingCount: Long? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("lastLocation") val lastLocation: LocationStruct? = null
)