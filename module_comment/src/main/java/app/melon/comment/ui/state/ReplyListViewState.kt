package app.melon.comment.ui.state

import app.melon.data.entities.Comment

data class ReplyListViewState(
    val id: String,
    val viewComment: Comment? = null,
    val loading: Boolean = false,
    val error: Throwable? = null
)