package app.melon.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Comment(
    @SerializedName("id") val id: String = "",
    @SerializedName("poster") val displayPoster: User = User(),
    @SerializedName("content") val content: String = "",
    @SerializedName("reply_count") val replyCount: Int = 0,
    @SerializedName("favor_count") val favorCount: Int = 0,
    @SerializedName("post_time") val postTime: String = "",
    @SerializedName("quote") val quote: Comment? = null
) : Serializable