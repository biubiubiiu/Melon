package app.melon.feed.data.remote

import app.melon.data.remote.LocationStruct
import com.google.gson.annotations.SerializedName

data class FeedListItemResponse(
    @SerializedName("id") val id: String = "",
    @SerializedName("user") val user: FeedListUserStruct? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("text") val content: String? = null,
    @SerializedName("photos") val photos: List<String>? = null,
    @SerializedName("location") val location: LocationStruct? = null,
    @SerializedName("favorCount") val favorCount: Long? = null,
    @SerializedName("commentCount") val replyCount: Long? = null,
    @SerializedName("isCollected") val isCollected: Boolean = false,
    @SerializedName("isFavored") val isFavor: Boolean = false,
    @SerializedName("postTime") val postTime: String? = null
)

data class FeedListUserStruct(
    @SerializedName("id") val id: String = "",
    @SerializedName("username") val username: String? = null,
    @SerializedName("customId") val customId: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("school") val school: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("gender") val gender: String? = null
)