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
    @ColumnInfo(name = "author_uid") val authorUid: String = "",
    @ColumnInfo(name = "group_id") val groupId: String? = null,
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "content") val content: String = "",
    @ColumnInfo(name = "photo_list") val photos: List<String> = emptyList(),
    @ColumnInfo(name = "post_time") val postTime: String = "",
    @ColumnInfo(name = "comment") val replyCount: Long = 0L,
    @ColumnInfo(name = "favor") val favouriteCount: Long = 0L // TODO add is_anonymous field
) : MelonEntity