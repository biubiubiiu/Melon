package app.melon.feed

import android.content.Context
import app.melon.data.entities.Feed
import app.melon.user.api.IUserService
import app.melon.util.extensions.showToast
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class FeedControllerDelegate @AssistedInject constructor(
    @Assisted private val context: Context,
    private val userService: IUserService
) : FeedActions {

    fun buildFeedItem(
        feedProvider: () -> Feed,
        idProvider: () -> String = { "feed_${feedProvider.invoke().feedId}" }
    ): EpoxyModel<*> {
        val feed = feedProvider.invoke()
        return FeedItem_()
            .id(idProvider.invoke())
            .item(feed)
            .holderClickListener { this.onHolderClick() }
            .avatarClickListener { this.onAvatarClick(feed.postId) }
            .shareClickListener { this.onShareClick() }
            .commentClickListener { this.onCommentClick() }
            .favorClickListener { this.onFavorClick() }
            .moreClickListener { this.onMoreClick() }
            .photoClickListener { urls, position -> this.onPhotoClick(urls, position) }
    }

    override fun onHolderClick() {
        context.showToast("click holder")
    }

    override fun onAvatarClick(uid: String) {
        userService.navigateToUserProfile(context, uid)
    }

    override fun onShareClick() {
        context.showToast("click share")
    }

    override fun onCommentClick() {
        context.showToast("click comment")
    }

    override fun onFavorClick() {
        context.showToast("click favor")
    }

    override fun onMoreClick() {
        context.showToast("click more")
    }

    override fun onPhotoClick(urls: List<String>, position: Int) {
        context.showToast("Click item $position, urls size: ${urls.size}")
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context): FeedControllerDelegate
    }
}