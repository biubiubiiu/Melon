package app.melon.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "feeds",
    indices = [
        Index(value = ["author_uid"], unique = false),
        Index(value = ["group_id"], unique = false)
    ]
)
data class Feed(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "feed_id") override val id: String = "",
    @ColumnInfo(name = "author_uid") val authorUid: String? = null,
    @ColumnInfo(name = "group_id") val groupId: String? = null,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "content") val content: String? = null,
    @ColumnInfo(name = "photo_list") val photos: List<String> = emptyList(),
    @ColumnInfo(name = "poi_info") val poiInfo: PoiInfo? = null,
    @ColumnInfo(name = "post_time") val postTime: String? = null,
    @ColumnInfo(name = "is_collected") val isCollected: Boolean = false,
    @ColumnInfo(name = "is_favored") val isFavored: Boolean = false,
    @ColumnInfo(name = "repost_count") val repostCount: Long? = null,
    @ColumnInfo(name = "reply_count") val replyCount: Long? = null,
    @ColumnInfo(name = "favor_count") val favouriteCount: Long? = null
) : MelonEntity