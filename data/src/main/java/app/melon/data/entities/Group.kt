package app.melon.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "groups"
)
data class Group(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "group_id") override val id: String = "", // TODO should we add founder_uid?
    @ColumnInfo(name = "group_name") val name: String = "",
    @ColumnInfo(name = "icon_url") val iconUrl: String = "",
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "members_count") val membersCount: Long? = null
) : MelonEntity
