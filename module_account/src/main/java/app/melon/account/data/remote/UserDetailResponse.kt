package app.melon.account.data.remote

import com.google.gson.annotations.SerializedName

internal data class UserDetailResponse(
    @SerializedName("id") val uid: String = "",
    @SerializedName("username") val username: String? = null,
    @SerializedName("customId") val customId: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("school") val school: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null
)