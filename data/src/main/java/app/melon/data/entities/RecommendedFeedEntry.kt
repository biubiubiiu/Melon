package app.melon.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import app.melon.data.constants.FeedType

@Entity(
    tableName = "recommended_feeds",
    indices = [
        Index(value = ["feed_id"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = Feed::class,
            parentColumns = arrayOf("feed_id"),
            childColumns = arrayOf("feed_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecommendedFeedEntry(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "feed_id") override val feedId: String,
    @ColumnInfo(name = "page") override val page: Int,
    @ColumnInfo(name = "page_order") override val pageOrder: Int,
    override val feedType: String = FeedType.RecommendFeed
) : FeedEntry