package app.melon.feed

interface FeedActions {
    fun onHolderClick()
    fun onAvatarClick(uid: String)
    fun onShareClick()
    fun onCommentClick()
    fun onFavorClick()
    fun onMoreClick()
    fun onSaveImage(url: String)
}