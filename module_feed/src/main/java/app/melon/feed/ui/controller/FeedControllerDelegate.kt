package app.melon.feed.ui.controller

import android.content.Context
import app.melon.account.api.UserManager
import app.melon.base.ui.extensions.activityContext
import app.melon.comment.ICommentService
import app.melon.composer.api.Commentary
import app.melon.composer.api.ComposerEntry
import app.melon.data.entities.Feed
import app.melon.data.entities.PoiInfo
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.FeedActions
import app.melon.feed.FeedActionsFragment
import app.melon.feed.FeedDetailActivity
import app.melon.feed.IFeedService
import app.melon.feed.ui.widget.AnonymousFeedItem_
import app.melon.feed.ui.widget.FeedItem_
import app.melon.location.LocationHelper
import app.melon.poi.api.IPoiService
import app.melon.user.api.IUserService
import app.melon.util.extensions.showToast
import app.melon.util.formatter.MelonDateTimeFormatter
import app.melon.util.formatter.MelonDistanceFormatter
import app.melon.util.formatter.MelonNumberFormatter
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class FeedControllerDelegate @AssistedInject constructor(
    @Assisted private val context: Context,
    private val feedService: IFeedService,
    private val userService: IUserService,
    private val poiService: IPoiService,
    private val commentService: ICommentService,
    private val userManager: UserManager,
    private val locationHelper: LocationHelper,
    private val dateTimeFormatter: MelonDateTimeFormatter,
    private val numberFormatter: MelonNumberFormatter,
    private val distanceFormatter: MelonDistanceFormatter
) : FeedActions {

    fun buildFeedItem(
        dataProvider: () -> FeedAndAuthor,
        idProvider: () -> String = { "feed_${dataProvider.invoke().feed.id}" }
    ): EpoxyModel<*> {
        val item = dataProvider.invoke()
        return FeedItem_()
            .id(idProvider.invoke())
            .item(item)
            .locationHelper(locationHelper)
            .formatter(dateTimeFormatter)
            .numberFormatter(numberFormatter)
            .distanceFormatter(distanceFormatter)
            .holderClickListener { this.onHolderClick(it) }
            .avatarClickListener { this.onAvatarClick(it) }
            .shareClickListener { this.onShareClick(it) }
            .commentClickListener { this.onCommentClick(it) }
            .favorClickListener { this.onFavorClick(it) }
            .moreClickListener { this.onMoreClick(it) }
            .poiEntryClickListener { this.onPoiEntryClick(it) }
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

    override fun onCommentClick(item: FeedAndAuthor) {
        val user = userManager.user ?: return
        (context.activityContext as? ComposerEntry)?.launchComposer(
            option = Commentary(
                authorUid = item.author.id,
                authorUsername = item.author.username.orEmpty(),
                accountAvatarUrl = user.avatarUrl
            ),
            callback = { result ->
                val (content, _, _) = result ?: return@launchComposer
                commentService.postComment(context, item.feed.id, content)
            }
        )
    }

    override fun onFavorClick(id: String) {
        context.showToast("click favor")
    }

    override fun onMoreClick(feed: Feed) {
        context.activityContext?.let { activity ->
            val dialog = FeedActionsFragment.newInstance(feed)
            dialog.onCollectListener = {
                feedService.collect(feed.id)
            }
            dialog.show(activity.supportFragmentManager, "actions")
        }
    }

    override fun onPoiEntryClick(info: PoiInfo) {
        poiService.navigateToPoiDetail(context, info)
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context): FeedControllerDelegate
    }
}