package app.melon.data.entities

import java.io.Serializable

data class Comment(
    val id: String = "",
    val authorUid: String? = null,
    val content: String? = null,
    val replyCount: Long? = null,
    val favorCount: Long? = null,
    val postTime: String? = null,
    val quote: Comment? = null
) : Serializable