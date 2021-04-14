package app.melon.comment

import android.content.Context
import androidx.annotation.ColorRes
import app.melon.base.ui.list.loadMoreView
import app.melon.comment.ui.widget.commentItem
import app.melon.data.resultentities.CommentAndAuthor
import app.melon.user.api.IUserService
import app.melon.util.extensions.showToast
import app.melon.util.time.MelonDateTimeFormatter
import com.airbnb.epoxy.ModelCollector
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class CommentControllerDelegate @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val collector: ModelCollector,
    private val userService: IUserService,
    private val dateTimeFormatter: MelonDateTimeFormatter
) : CommentActions {

    fun buildCommentList(
        list: List<CommentAndAuthor>?,
        loading: Boolean,
        displayReplyCount: Boolean = true,
        @ColorRes backgroundRes: Int = R.color.bgPrimary
    ) = with(collector) {
        list?.forEachIndexed { index, item ->
            commentItem {
                id("comment_${index}")
                item(item)
                formatter(dateTimeFormatter)
                displayReplyCount(displayReplyCount)
                backgroundRes(backgroundRes)
                avatarClickListener { userService.navigateToUserProfile(context, item.author.id) }
                shareClickListener { context.showToast("Click share") }
                replyClickListener { context.showToast("Click reply") }
                favorClickListener { context.showToast("Click favor") }
                subCommentEntryClickListener { onSubCommentEntryClick(it) }
            }
        }
        if (loading) {
            loadMoreView {
                id("comment_loading_more")
            }
        }
    }

    override fun onSubCommentEntryClick(id: String) {
        CommentReplyActivity.start(context, id)
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, collector: ModelCollector): CommentControllerDelegate
    }
}