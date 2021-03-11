package app.melon.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import app.melon.data.PaginatedEntry

@Entity(
    tableName = "recommended_feeds",
    indices = [
        Index(value = ["feed_id"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = Feed::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("feed_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecommendFeedEntry(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "feed_id") override val feedId: Long,
    @ColumnInfo(name = "page") override val page: Int,
    @ColumnInfo(name = "page_order") val pageOrder: Int
) : PaginatedEntry