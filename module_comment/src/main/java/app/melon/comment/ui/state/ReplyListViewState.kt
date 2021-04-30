package app.melon.comment.ui.state

import app.melon.data.resultentities.CommentAndAuthor

internal data class ReplyListViewState(
    val id: String,
    val viewComment: CommentAndAuthor? = null,
    val error: Throwable? = null
)