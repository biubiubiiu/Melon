package app.melon.account.login.state


/**
 * Authentication result : success (token) or error message.
 */
data class LoginResult(
    val success: String? = null,
    val error: Int? = null
)