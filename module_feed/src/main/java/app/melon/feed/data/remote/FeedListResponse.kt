package app.melon.feed.data.remote

import com.google.gson.annotations.SerializedName

data class FeedListItemResponse(
    @SerializedName("id") val id: String = "",
    @SerializedName("user") val user: FeedListUserStruct = FeedListUserStruct(),
    @SerializedName("title") val title: String? = null,
    @SerializedName("text") val content: String = "",
    @SerializedName("fileUrlList") val photos: List<String>? = null,
    @SerializedName("location") val location: String? = null, // TODO define location struct
    @SerializedName("likeNum") val favorCount: Long? = null,
    @SerializedName("remarkNum") val replyCount: Long? = null,
    @SerializedName("like") val isFavor: Boolean = false,
    @SerializedName("postTimestamp") val postTime: String? = null,
    @SerializedName("lastUpdateTimestamp") val lastUpdateTime: String? = null
)

data class FeedListUserStruct(
    @SerializedName("id") val id: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("avatarURL") val avatarUrl: String? = null,
    @SerializedName("school") val school: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("lastLocation") val lastLocation: String? = null
)