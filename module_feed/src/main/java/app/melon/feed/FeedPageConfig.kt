package app.melon.feed

import app.melon.data.constants.FeedPageType

data class FeedPageConfig(
    @FeedPageType val pageType: Int,
    val idPrefix: String, // Used as epoxy model id prefix
    val isAnonymousFeed: Boolean // TODO remove this field
)