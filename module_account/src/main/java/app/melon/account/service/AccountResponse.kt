package app.melon.account.service

import com.google.gson.annotations.SerializedName

data class AccountResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("results") val result: AccountResult? = null
)

data class AccountResult(
    @SerializedName("tokenHeader") val header: String? = null,
    @SerializedName("token") val token: String? = null
)