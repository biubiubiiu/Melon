package app.melon.feed.ui.state

import app.melon.data.entities.Comment
import app.melon.data.entities.Feed

data class FeedDetailViewState(
    val feed: Feed? = null,
    val comments: List<Comment> = emptyList(),
    val refreshing: Boolean = false,
    val error: Throwable? = null
)