package app.melon.user.data.remote

import com.google.gson.annotations.SerializedName

internal data class UpdateAvatarResponse(
    @SerializedName("avatarURL") val avatarUrl: String? = null
)