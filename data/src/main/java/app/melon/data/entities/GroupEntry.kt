package app.melon.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import app.melon.data.constants.GroupPageType


@Entity(
    tableName = "group_entries",
    indices = [
        Index(value = ["group_id"], unique = false)
    ]
)
data class GroupEntry(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0L,
    @ColumnInfo(name = "group_id") override val relatedId: String,
    @ColumnInfo(name = "page") override val page: Int,
    @ColumnInfo(name = "page_order") override val pageOrder: Int,
    @ColumnInfo(name = "page_type") @GroupPageType val pageType: Int
) : PaginatedEntry