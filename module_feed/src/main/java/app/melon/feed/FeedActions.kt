package app.melon.feed

import app.melon.data.entities.Feed
import app.melon.data.resultentities.FeedAndAuthor

interface FeedActions {
    fun onHolderClick(feed: FeedAndAuthor)
    fun onAvatarClick(uid: String)
    fun onShareClick(feed: Feed)
    fun onCommentClick(feed: Feed)
    fun onFavorClick(id: String)
    fun onMoreClick(feed: Feed)
    fun onSaveImage(url: String)
}