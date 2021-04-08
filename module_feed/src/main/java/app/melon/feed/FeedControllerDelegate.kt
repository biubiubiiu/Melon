package app.melon.feed

import android.content.Context
import app.melon.data.entities.Feed
import app.melon.feed.ui.AnonymousFeedItem_
import app.melon.feed.ui.FeedItem_
import app.melon.permission.helper.SaveHelper
import app.melon.user.api.IUserService
import app.melon.util.extensions.activityContext
import app.melon.util.extensions.showToast
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class FeedControllerDelegate @AssistedInject constructor(
    @Assisted private val context: Context,
    private val userService: IUserService
) : FeedActions {

    private val saveHelper by lazy { SaveHelper(context.activityContext!!) }

    fun buildFeedItem(
        feedProvider: () -> Feed,
        idProvider: () -> String = { "feed_${feedProvider.invoke().feedId}" }
    ): EpoxyModel<*> {
        val feed = feedProvider.invoke()
        return FeedItem_()
            .id(idProvider.invoke())
            .item(feed)
            .holderClickListener { this.onHolderClick(it) }
            .avatarClickListener { this.onAvatarClick(it) }
            .shareClickListener { this.onShareClick(it) }
            .commentClickListener { this.onCommentClick(it) }
            .favorClickListener { this.onFavorClick(it) }
            .moreClickListener { this.onMoreClick(it) }
            .saveImageListener { this.onSaveImage(it) }
    }

    fun buildAnonymousFeedItem(
        feedProvider: () -> Feed,
        idProvider: () -> String = { "feed_${feedProvider.invoke().feedId}" }
    ): EpoxyModel<*> {
        val feed = feedProvider.invoke()
        return AnonymousFeedItem_()
            .id(idProvider.invoke())
            .item(feed)
            .holderClickListener { this.onHolderClick(it) }
            .shareClickListener { this.onShareClick(it) }
            .commentClickListener { this.onCommentClick(it) }
            .favorClickListener { this.onFavorClick(it) }
            .moreClickListener { this.onMoreClick(it) }
    }

    override fun onHolderClick(feed: Feed) {
        val intent = FeedDetailActivity.prepareIntent(context, feed)
        context.startActivity(intent)
    }

    override fun onAvatarClick(uid: String) {
        userService.navigateToUserProfile(context, uid)
    }

    override fun onShareClick(feed: Feed) {
        context.showToast("click share")
    }

    override fun onCommentClick(feed: Feed) {
        context.showToast("click comment")
    }

    override fun onFavorClick(id: String) {
        context.showToast("click favor")
    }

    override fun onMoreClick(feed: Feed) {
        context.showToast("click more")
    }

    override fun onSaveImage(url: String) {
        saveHelper.saveImage(url)
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context): FeedControllerDelegate
    }
}