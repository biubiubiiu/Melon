package app.melon.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import app.melon.data.constants.FeedPageType


@Entity(
    tableName = "feed_entries",
    indices = [
        Index(value = ["feed_id"], unique = false)
    ]
)
data class FeedEntry(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0L,
    @ColumnInfo(name = "feed_id") override val relatedId: String,
    @ColumnInfo(name = "page") override val page: Int,
    @ColumnInfo(name = "page_order") override val pageOrder: Int,
    @ColumnInfo(name = "page_type") @FeedPageType val pageType: Int
) : PaginatedEntry