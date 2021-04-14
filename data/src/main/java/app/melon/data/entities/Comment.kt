package app.melon.data.entities

import java.io.Serializable

data class Comment(
    val id: String = "",
    val authorUid: String = "",
    val content: String = "",
    val replyCount: Long = 0L,
    val favorCount: Long = 0L,
    val postTime: String = "",
    val quote: Comment? = null
) : Serializable