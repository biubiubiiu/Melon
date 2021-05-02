package app.melon.account.signup.state

internal data class SignUpResult(
    val success: Boolean,
    val error: Int? = null
)