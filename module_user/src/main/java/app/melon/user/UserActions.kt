package app.melon.user

import app.melon.data.entities.User

internal interface UserActions {
    fun onHolderClick(user: User)
}