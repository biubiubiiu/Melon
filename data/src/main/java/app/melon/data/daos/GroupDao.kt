package app.melon.data.daos

import androidx.room.Dao
import androidx.room.Query
import app.melon.data.daos.base.EntityDao
import app.melon.data.entities.Group


@Dao
abstract class GroupDao : EntityDao<Group>() {

    @Query("SELECT * FROM groups WHERE group_id = :id")
    abstract suspend fun getGroupWithId(id: String): Group?

    suspend fun getGroupWithIdOrThrow(id: String): Group {
        return getGroupWithId(id) ?: throw IllegalArgumentException("No group with id $id in database")
    }

    @Query("DELETE FROM groups WHERE group_id = :id")
    abstract suspend fun delete(id: String)

    @Query("DELETE FROM groups")
    abstract suspend fun deleteAll()

    override suspend fun insertOrUpdate(entity: Group) {
        val item = getGroupWithId(entity.id)
        if (item == null) {
            insert(entity)
        } else {
            update(entity)
        }
    }
}