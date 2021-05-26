package app.melon.comment.data.remote

import com.google.gson.annotations.SerializedName

data class CommentStruct(
    @SerializedName("id") val id: String = "",
    @SerializedName("postId") val feedId: String? = null,
    @SerializedName("text") val content: String? = null,
    @SerializedName("user") val author: CommentAuthorStruct? = null,
    @SerializedName("likeNum") val favorCount: Long? = null,
    @SerializedName("replyNum") val replyCount: Long? = null,
    @SerializedName("timestamp") val postTime: String? = null,
    @SerializedName("like") val isFavor: Boolean = false,
    @SerializedName("quote") val quote: CommentStruct? = null
)

data class CommentAuthorStruct(
    @SerializedName("id") val id: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("customId") val customId: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null
)