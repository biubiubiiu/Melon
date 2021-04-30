package app.melon.comment

import app.melon.data.entities.Comment

internal interface CommentActions {
    fun onAvatarClick(uid: String)
    fun onShareClick(item: Comment)
    fun onReplyClick(item: Comment)
    fun onFavorClick(id: String)
    fun onSubCommentEntryClick(id: String)
}