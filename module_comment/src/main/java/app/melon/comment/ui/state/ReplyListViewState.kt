package app.melon.comment.ui.state

import app.melon.data.resultentities.CommentAndAuthor

data class ReplyListViewState(
    val id: String,
    val viewComment: CommentAndAuthor? = null,
    val loading: Boolean = false,
    val error: Throwable? = null
)