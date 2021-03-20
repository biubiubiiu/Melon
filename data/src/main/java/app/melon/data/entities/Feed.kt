package app.melon.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(
    tableName = "feeds",
    indices = [
        Index(value = ["feed_id"], unique = true)
    ]
)
data class Feed(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
    @SerializedName("feed_id") @ColumnInfo(name = "feed_id") override val feedId: String = "",
    @SerializedName("avatar_url") @ColumnInfo(name = "avatar_url") val avatarUrl: String = "",
    @SerializedName("username") @ColumnInfo(name = "username") val username: String = "",
    @SerializedName("user_id") @ColumnInfo(name = "user_id") val userId: String = "",
    @SerializedName("school") @ColumnInfo(name = "school") val school: String = "",
    @SerializedName("post_time") @ColumnInfo(name = "post_time") val postTime: Long = 0L,
    @SerializedName("content") @ColumnInfo(name = "content") val content: String = "",
    @SerializedName("comment") @ColumnInfo(name = "comment") val replyCount: Long = 0L,
    @SerializedName("favor") @ColumnInfo(name = "favor") val favouriteCount: Long = 0L,
    @ColumnInfo(name = "feed_type") override val type: String = ""
) : FeedEntity {
    @Ignore
    constructor() : this(0)

    companion object {
        val EMPTY_FEED = Feed()
    }
}