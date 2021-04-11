package app.melon.account.login.state


/**
 * Authentication result : success or error message.
 */
data class LoginResult(
    val success: Boolean = false,
    val error: Int? = null
)