package app.melon.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
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
    @SerializedName("fid") @ColumnInfo(name = "feed_id") override val feedId: String = "",
    @SerializedName("avatar_url") @ColumnInfo(name = "avatar_url") val avatarUrl: String = "",
    @SerializedName("username") @ColumnInfo(name = "username") val username: String = "",
    @SerializedName("user_id") @ColumnInfo(name = "user_id") val userId: String = "",
    @SerializedName("poster_id") @ColumnInfo(name = "poster_id") val postId: String = "",
    @SerializedName("school") @ColumnInfo(name = "school") val school: String = "",
    @SerializedName("post_time") @ColumnInfo(name = "post_time") val postTime: String = "",
    @SerializedName("photo_list") @ColumnInfo(name = "photo_list") val photos: List<String> = emptyList(),
    @SerializedName("title") @ColumnInfo(name = "title") val title: String = "",
    @SerializedName("content") @ColumnInfo(name = "content") val content: String = "",
    @SerializedName("comment") @ColumnInfo(name = "comment") val replyCount: Long = 0L,
    @SerializedName("favor") @ColumnInfo(name = "favor") val favouriteCount: Long = 0L,
    @ColumnInfo(name = "feed_type") override val type: String = ""
) : FeedEntity