package app.melon.data.daos

import androidx.room.Dao
import androidx.room.Query
import app.melon.data.daos.base.EntityDao
import app.melon.data.entities.JoinGroup


@Dao
abstract class JoinGroupDao : EntityDao<JoinGroup>() {

    @Query("SELECT * FROM join_group WHERE group_id = :groupId AND user_id = :userId")
    abstract suspend fun getAttendEventRecord(groupId: String, userId: String): JoinGroup?

    override suspend fun insertOrUpdate(entity: JoinGroup) {
        val item = getAttendEventRecord(entity.groupId, entity.userId)
        if (item == null) {
            insert(entity)
        } else {
            update(entity)
        }
    }
}