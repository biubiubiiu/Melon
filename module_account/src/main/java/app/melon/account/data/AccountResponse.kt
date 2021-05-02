package app.melon.account.data

import com.google.gson.annotations.SerializedName

internal data class AccountResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("results") val result: AccountResult? = null
)

internal data class AccountResult(
    @SerializedName("tokenHeader") val header: String? = null,
    @SerializedName("token") val token: String? = null
)