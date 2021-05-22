package app.melon.feed

import android.content.Context
import app.melon.account.api.UserManager
import app.melon.base.ui.extensions.activityContext
import app.melon.comment.ICommentService
import app.melon.composer.api.Commentary
import app.melon.composer.api.ComposerEntry
import app.melon.data.entities.Feed
import app.melon.data.entities.PoiInfo
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.poi.api.IPoiService
import app.melon.user.api.IUserService
import app.melon.util.extensions.showToast
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class FeedActionHandler @AssistedInject constructor(
    @Assisted private val context: Context,
    private val feedService: IFeedService,
    private val userService: IUserService,
    private val poiService: IPoiService,
    private val commentService: ICommentService,
    private val userManager: UserManager
) : FeedActions {

    override fun onHolderClick(feed: FeedAndAuthor) {
        val intent = FeedDetailActivity.prepareIntent(context, feed)
        context.startActivity(intent)
    }

    override fun onAvatarClick(uid: String) {
        userService.navigateToUserProfile(context, uid)
    }

    override fun onRepostClick(feed: Feed) {
        context.showToast("repost under construction")
    }

    override fun onShareClick(feed: Feed) {
        context.showToast("share under construction")
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
        fun create(context: Context): FeedActionHandler
    }
}