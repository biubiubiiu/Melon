package app.melon.user.interactor

import app.melon.data.MelonDatabase
import app.melon.data.entities.User
import javax.inject.Inject

internal class SyncAvatarUpdate @Inject constructor(
    private val database: MelonDatabase
) {

    suspend fun updateAvatar(uid: String, url: String) {
        database.runWithTransaction {
            val localUser = database.userDao().getUserWithId(uid) ?: User(id = uid)
            database.userDao().insertOrUpdate(localUser.copy(avatarUrl = url))
        }
    }
}