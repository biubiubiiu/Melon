package app.melon.feed.ui.state

import app.melon.data.resultentities.CommentAndAuthor
import app.melon.data.resultentities.FeedAndAuthor

data class FeedDetailViewState(
    val pageItem: FeedAndAuthor? = null,
    val comments: List<CommentAndAuthor> = emptyList(),
    val refreshing: Boolean = false,
    val error: Throwable? = null
)