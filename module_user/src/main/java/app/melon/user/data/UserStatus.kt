package app.melon.user.data

import app.melon.data.entities.User


internal sealed class UserStatus<out T : Any> {

    class Success(val data: User) : UserStatus<User>()

    sealed class Error(val throwable: Throwable) : UserStatus<Nothing>() {
        class Generic(throwable: Throwable) : Error(throwable)
    }
}