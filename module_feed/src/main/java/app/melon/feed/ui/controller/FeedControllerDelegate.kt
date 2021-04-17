package app.melon.feed.ui.controller

import android.content.Context
import app.melon.data.entities.Feed
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.FeedActions
import app.melon.feed.FeedDetailActivity
import app.melon.feed.ui.widget.AnonymousFeedItem_
import app.melon.feed.ui.widget.FeedItem_
import app.melon.permission.helper.SaveHelper
import app.melon.user.api.IUserService
import app.melon.util.extensions.activityContext
import app.melon.util.extensions.showToast
import app.melon.util.number.MelonNumberFormatter
import app.melon.util.formatter.MelonDateTimeFormatter
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class FeedControllerDelegate @AssistedInject constructor(
    @Assisted private val context: Context,
    private val userService: IUserService,
    private val dateTimeFormatter: MelonDateTimeFormatter,
    private val numberFormatter: MelonNumberFormatter
) : FeedActions {

    private val saveHelper by lazy { SaveHelper(context.activityContext!!) }

    fun buildFeedItem(
        dataProvider: () -> FeedAndAuthor,
        idProvider: () -> String = { "feed_${dataProvider.invoke().feed.id}" }
    ): EpoxyModel<*> {
        val item = dataProvider.invoke()
        return FeedItem_()
            .id(idProvider.invoke())
            .item(item)
            .formatter(dateTimeFormatter)
            .numberFormatter(numberFormatter)
            .holderClickListener { this.onHolderClick(it) }
            .avatarClickListener { this.onAvatarClick(it) }
            .shareClickListener { this.onShareClick(it) }
            .commentClickListener { this.onCommentClick(it) }
            .favorClickListener { this.onFavorClick(it) }
            .moreClickListener { this.onMoreClick(it) }
            .saveImageListener { this.onSaveImage(it) }
    }

    fun buildAnonymousFeedItem(
        dataProvider: () -> FeedAndAuthor,
        idProvider: () -> String = { "feed_${dataProvider.invoke().feed.id}" }
    ): EpoxyModel<*> {
        val item = dataProvider.invoke()
        return AnonymousFeedItem_()
            .id(idProvider.invoke())
            .item(item)
            .formatter(dateTimeFormatter)
            .numberFormatter(numberFormatter)
            .holderClickListener { this.onHolderClick(it) }
            .shareClickListener { this.onShareClick(it) }
            .commentClickListener { this.onCommentClick(it) }
            .favorClickListener { this.onFavorClick(it) }
            .moreClickListener { this.onMoreClick(it) }
    }

    override fun onHolderClick(feed: FeedAndAuthor) {
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