package app.melon.comment

import android.content.Context
import app.melon.base.ui.list.loadMoreView
import app.melon.comment.ui.commentItem
import app.melon.data.entities.Comment
import app.melon.user.api.IUserService
import app.melon.util.extensions.showToast
import com.airbnb.epoxy.ModelCollector
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class CommentControllerDelegate @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val collector: ModelCollector,
    private val userService: IUserService
) : CommentActions {

    fun buildCommentList(list: List<Comment>?, loading: Boolean) = with(collector) {
        list?.forEachIndexed { index, comment ->
            commentItem {
                id("comment_${index}")
                item(comment)
                avatarClickListener { userService.navigateToUserProfile(context, comment.displayPoster.id) }
                shareClickListener { context.showToast("Click share") }
                replyClickListener { context.showToast("Click reply") }
                favorClickListener { context.showToast("Click favor") }
                replyEntryClickListener { context.showToast("Click reply entry") }
            }
        }
        if (loading) {
            loadMoreView {
                id("comment_loading_more")
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, collector: ModelCollector): CommentControllerDelegate
    }
}