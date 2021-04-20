package app.melon.feed.data.remote

import app.melon.data.remote.LocationStruct
import com.google.gson.annotations.SerializedName

data class FeedDetailResponse(
    @SerializedName("id") val id: String = "",
    @SerializedName("user") val user: FeedDetailUserStruct? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("text") val content: String? = null,
    @SerializedName("fileUrlList") val photos: List<String> = emptyList(),
    @SerializedName("location") val location: LocationStruct? = null,
    @SerializedName("remarkNum") val replyCount: Long? = null,
    @SerializedName("likeNum") val favouriteCount: Long? = null,
    @SerializedName("like") val isFavor: Boolean = false,
    @SerializedName("postTimestamp") val postTime: String? = null,
    @SerializedName("lastUpdateTimestamp") val lastUpdateTime: String? = null
)

data class FeedDetailUserStruct(
    @SerializedName("id") val id: String = "",
    @SerializedName("username") val username: String? = null,
    @SerializedName("avatarURL") val avatarUrl: String? = null,
    @SerializedName("school") val school: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("lastLocation") val lastLocation: LocationStruct? = null
)