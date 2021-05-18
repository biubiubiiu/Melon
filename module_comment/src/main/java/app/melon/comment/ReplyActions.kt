package app.melon.comment

import app.melon.data.entities.Comment

internal interface ReplyActions {
    fun onShareClick(item: Comment)
    fun onReplyClick(item: Comment)
    fun onFavorClick(id: String)
    fun onProfileEntryClick(id: String)
}