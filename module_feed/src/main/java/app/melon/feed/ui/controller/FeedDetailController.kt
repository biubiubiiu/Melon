package app.melon.feed.ui.controller

import android.content.Context
import app.melon.comment.CommentControllerDelegate
import app.melon.data.entities.Comment
import app.melon.data.entities.Feed
import app.melon.feed.ui.widget.feedHeader
import app.melon.user.api.IUserService
import app.melon.util.extensions.showToast
import com.airbnb.epoxy.Typed3EpoxyController
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class FeedDetailController @AssistedInject constructor(
    @Assisted private val context: Context,
    commentControllerFactory: CommentControllerDelegate.Factory,
    private val userService: IUserService
) : Typed3EpoxyController<Feed, List<Comment>, Boolean>() {

    private val commentDelegate = commentControllerFactory.create(context, this)

    override fun buildModels(feed: Feed?, commentList: List<Comment>?, loadingComment: Boolean) {
        if (feed != null) {
            feedHeader {
                id("feed_detail_header")
                item(feed)
                avatarClickListener { userService.navigateToUserProfile(context, feed.userId) }
                repostEntryClickListener { context.showToast("Click repost entry") }
                favorEntryClickListener { context.showToast("Click favor entry") }
                commentClickListener { context.showToast("Click comment") }
                favorClickListener { context.showToast("Click favor") }
                shareClickListener { context.showToast("Click share") }
                moreClickListener { context.showToast("Click more") }
            }
        }
        commentDelegate.buildCommentList(commentList, loadingComment)
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context): FeedDetailController
    }
}