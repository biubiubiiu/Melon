package app.melon.data.entities

data class Comment(
    val id: String = "",
    val displayPoster: User = User(),
    val content: String = "",
    val replyCount: Int = 0,
    val favorCount: Int = 0,
    val displayReply: List<Comment> = emptyList(),
    val postTime: Long = 0L
) {
    val hasMoreReply get() = replyCount > displayReply.size
}