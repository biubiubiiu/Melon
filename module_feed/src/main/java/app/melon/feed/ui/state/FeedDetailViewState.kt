package app.melon.feed.ui.state

import app.melon.data.resultentities.FeedAndAuthor

data class FeedDetailViewState(
    val pageItem: FeedAndAuthor? = null,
    val refreshing: Boolean = false,
    val error: Throwable? = null
)