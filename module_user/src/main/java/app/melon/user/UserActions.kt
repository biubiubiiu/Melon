package app.melon.user

import app.melon.data.entities.User

interface UserActions {
    fun onHolderClick(user: User)
}