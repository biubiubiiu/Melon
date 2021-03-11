package app.melon.data.dto

import com.google.gson.annotations.SerializedName

data class BaseApiResponse<T>(
    @SerializedName("status_code") val code: String = "",
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: T? = null
)