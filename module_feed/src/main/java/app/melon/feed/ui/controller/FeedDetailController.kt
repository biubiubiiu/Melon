package app.melon.feed.ui.controller

import android.content.Context
import app.melon.account.api.UserManager
import app.melon.base.framework.BasePagingController
import app.melon.base.ui.extensions.activityContext
import app.melon.comment.CommentControllerDelegate
import app.melon.comment.ICommentService
import app.melon.composer.api.Commentary
import app.melon.composer.api.ComposerEntry
import app.melon.data.entities.Feed
import app.melon.data.entities.PoiInfo
import app.melon.data.resultentities.CommentAndAuthor
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.FeedDetailActions
import app.melon.feed.ui.widget.feedHeader
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


class FeedDetailController @AssistedInject constructor(
    @Assisted context: Context,
    commentControllerFactory: CommentControllerDelegate.Factory,
    private val userService: IUserService,
    private val poiService: IPoiService,
    private val commentService: ICommentService,
    private val locationHelper: LocationHelper,
    private val userManager: UserManager,
    private val numberFormatter: MelonNumberFormatter,
    private val dateTimeFormatter: MelonDateTimeFormatter,
    private val distanceFormatter: MelonDistanceFormatter
) : FeedDetailActions, BasePagingController<CommentAndAuthor>(
    context,
    sameItemIndicator = { oldItem, newItem -> oldItem.comment.id == newItem.comment.id }
) {

    var item: FeedAndAuthor? = null
        set(value) {
            field = value
            requestModelBuild()
        }
    private val commentDelegate = commentControllerFactory.create(context, this)

    override fun addExtraModels() {
        item?.let { item ->
            feedHeader {
                id("feed_detail_header")
                item(item)
                locationHelper(locationHelper)
                numberFormatter(numberFormatter)
                distanceFormatter(distanceFormatter)
                formatter(dateTimeFormatter)
                avatarClickListener { onProfileEntryClick(it) }
                repostEntryClickListener { onRepostEntryClick(it) }
                favorEntryClickListener { onFavorEntryClick(it) }
                commentClickListener { onCommentClick(it) }
                favorClickListener { onFavorClick(it) }
                shareClickListener { onShareClick(it) }
                moreClickListener { onMoreClick(it) }
                poiEntryClickListener { onPoiEntryClick(it) }
            }
        }
    }

    override fun buildItemModel(currentPosition: Int, item: CommentAndAuthor?): EpoxyModel<*> {
        return commentDelegate.buildCommentItem(
            dataProvider = { item!! },
            idProvider = { "comment_$currentPosition" }
        )
    }

    override fun onProfileEntryClick(id: String) {
        userService.navigateToUserProfile(context, id)
    }

    override fun onRepostEntryClick(id: String) {
        context.showToast("Click repost entry")
    }

    override fun onFavorEntryClick(id: String) {
        context.showToast("Click favor entry")
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
        context.showToast("Click favor")
    }

    override fun onShareClick(feed: Feed) {
        context.showToast("Click share")
    }

    override fun onMoreClick(id: String) {
        context.showToast("Click more")
    }

    override fun onPoiEntryClick(item: PoiInfo) {
        poiService.navigateToPoiDetail(context, item)
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context): FeedDetailController
    }
}