package app.melon.user.data.remote

import app.melon.data.remote.LocationStruct
import com.google.gson.annotations.SerializedName

data class NearbyUserStruct(
    @SerializedName("id") val id: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("avatarURL") val avatarUrl: String? = null,
    @SerializedName("school") val school: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("lastLocation") val lastLocation: LocationStruct? = null
)