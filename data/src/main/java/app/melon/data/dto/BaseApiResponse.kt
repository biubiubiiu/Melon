package app.melon.data.dto

import com.google.gson.annotations.SerializedName

data class BaseApiResponse<T>(
    @SerializedName("code") val code: String = "",
    @SerializedName("message") val message: String = "",
    @SerializedName("results") val data: T? = null
) {
    val isSuccess get() = code == SUCCESS_CODE

    val errorMessage get() = "Error $code - $message"

    private companion object {
        const val SUCCESS_CODE = "200"
    }
}