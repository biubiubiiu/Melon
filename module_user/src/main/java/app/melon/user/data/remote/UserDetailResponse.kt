package app.melon.user.data.remote

import com.google.gson.annotations.SerializedName

data class UserDetailResponse(
    @SerializedName("id") val id: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("avatarURL") val avatarUrl: String? = null,
    @SerializedName("backgroundUrl") val backgroundUrl: String? = null,
    @SerializedName("school") val school: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("followers") val followerCount: Long? = null,
    @SerializedName("following") val followingCount: Long? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("lastLocation") val lastLocation: String? = null
)