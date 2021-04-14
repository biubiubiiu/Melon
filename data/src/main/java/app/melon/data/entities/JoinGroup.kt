package app.melon.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(
    tableName = "join_group",
    primaryKeys = ["group_id", "user_id"]
)
data class JoinGroup(
    @ColumnInfo(name = "group_id") val groupId: String,
    @ColumnInfo(name = "user_id") val userId: String
)