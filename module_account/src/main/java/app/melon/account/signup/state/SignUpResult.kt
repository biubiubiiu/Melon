package app.melon.account.signup.state

data class SignUpResult(
    val success: Boolean,
    val error: Int? = null
)