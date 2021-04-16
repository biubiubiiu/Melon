package app.melon.user.data.remote

import com.google.gson.annotations.SerializedName

data class UpdateAvatarResponse(
    @SerializedName("avatarURL") val avatarUrl: String? = null
)