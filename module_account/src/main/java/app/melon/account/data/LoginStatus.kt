package app.melon.account.data

sealed class LoginStatus<out T : Any> {

    class Success(val token: String) : LoginStatus<String>()

    object Failure : LoginStatus<Nothing>()

    sealed class Error(val throwable: Throwable) : LoginStatus<Nothing>() {
        class Generic(throwable: Throwable) : Error(throwable)
    }
}
