package app.melon.comment.data.remote

import com.google.gson.annotations.SerializedName

data class CommentStruct(
    @SerializedName("id") val id: String = "",
    @SerializedName("postId") val feedId: String = "",
    @SerializedName("text") val content: String = "",
    @SerializedName("user") val author: CommentAuthorStruct = CommentAuthorStruct(),
    @SerializedName("likeNum") val favorCount: Long? = null,
    @SerializedName("replyNum") val replyCount: Long? = null,
    @SerializedName("timestamp") val postTime: String = "",
    @SerializedName("like") val isFavor: Boolean = false,
    @SerializedName("quote") val quote: CommentStruct? = null
)

data class CommentAuthorStruct(
    @SerializedName("id") val id: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("avatarURL") val avatarUrl: String? = null
)