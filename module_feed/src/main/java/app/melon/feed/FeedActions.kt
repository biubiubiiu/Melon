package app.melon.feed

import app.melon.data.entities.Feed
import app.melon.data.entities.PoiInfo
import app.melon.data.resultentities.FeedAndAuthor

interface FeedActions {
    fun onHolderClick(feed: FeedAndAuthor)
    fun onAvatarClick(uid: String)
    fun onRepostClick(feed: Feed)
    fun onShareClick(feed: Feed)
    fun onCommentClick(item: FeedAndAuthor)
    fun onFavorClick(id: String)
    fun onMoreClick(feed: Feed)
    fun onPoiEntryClick(info: PoiInfo)
}