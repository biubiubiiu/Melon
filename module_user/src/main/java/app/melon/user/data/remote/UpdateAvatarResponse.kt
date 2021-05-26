package app.melon.user.data.remote

import com.google.gson.annotations.SerializedName

internal data class UpdateAvatarResponse(
    @SerializedName("avatarUrl") val avatarUrl: String? = null
)