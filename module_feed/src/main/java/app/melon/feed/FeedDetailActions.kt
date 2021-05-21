package app.melon.feed

import app.melon.data.entities.Feed
import app.melon.data.entities.PoiInfo
import app.melon.data.resultentities.FeedAndAuthor

interface FeedDetailActions {
    fun onProfileEntryClick(id: String)
    fun onRepostEntryClick(id: String)
    fun onFavorEntryClick(id: String)
    fun onCommentClick(item: FeedAndAuthor)
    fun onFavorClick(id: String)
    fun onShareClick(feed: Feed)
    fun onMoreClick(feed: Feed)
    fun onPoiEntryClick(item: PoiInfo)
}