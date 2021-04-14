package app.melon.data.daos

import androidx.room.Dao
import androidx.room.Query
import app.melon.data.daos.base.EntityDao
import app.melon.data.entities.User


@Dao
abstract class UserDao : EntityDao<User>() {

    @Query("SELECT * FROM users WHERE user_id = :id")
    abstract suspend fun getUserWithId(id: String): User?

    suspend fun getUserWithIdOrThrow(id: String): User {
        return getUserWithId(id) ?: throw IllegalArgumentException("No user with id $id in database")
    }

    @Query("DELETE FROM users WHERE user_id = :id")
    abstract suspend fun delete(id: String)

    @Query("DELETE FROM users")
    abstract suspend fun deleteAll()

    override suspend fun insertOrUpdate(entity: User) {
        val item = getUserWithId(entity.id)
        if (item == null) {
            insert(entity)
        } else {
            update(entity)
        }
    }
}