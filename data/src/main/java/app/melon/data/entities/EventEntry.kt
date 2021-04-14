package app.melon.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import app.melon.data.constants.EventPageType


@Entity(
    tableName = "event_entries",
    indices = [
        Index(value = ["event_id"], unique = false)
    ]
)
data class EventEntry(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0L,
    @ColumnInfo(name = "event_id") override val relatedId: String,
    @ColumnInfo(name = "page") override val page: Int,
    @ColumnInfo(name = "page_order") override val pageOrder: Int,
    @ColumnInfo(name = "page_type") @EventPageType val pageType: Int
) : PaginatedEntry