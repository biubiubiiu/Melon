package app.melon.comment

import android.content.Context
import androidx.annotation.ColorRes
import app.melon.comment.ui.widget.CommentItem_
import app.melon.comment.ui.widget.commentItem
import app.melon.data.entities.Comment
import app.melon.data.resultentities.CommentAndAuthor
import app.melon.user.api.IUserService
import app.melon.util.extensions.showToast
import app.melon.util.formatter.MelonDateTimeFormatter
import com.airbnb.epoxy.EpoxyModel
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

    fun buildCommentItem(
        dataProvider: () -> CommentAndAuthor,
        idProvider: () -> String = { "comment${dataProvider.invoke().comment.id}" },
        displayReplyCount: Boolean = true,
        @ColorRes backgroundRes: Int = R.color.bgPrimary
    ): EpoxyModel<*> {
        val item = dataProvider.invoke()
        return CommentItem_()
            .id(idProvider.invoke())
            .item(item)
            .formatter(dateTimeFormatter)
            .displayReplyCount(displayReplyCount)
            .backgroundRes(backgroundRes)
            .avatarClickListener { onAvatarClick(it) }
            .shareClickListener { onShareClick(it)}
            .replyClickListener { onReplyClick(it) }
            .favorClickListener { onFavorClick(it) }
            .subCommentEntryClickListener { onSubCommentEntryClick(it) }
    }

    internal fun buildCommentList(
        list: List<CommentAndAuthor?>,
        displayReplyCount: Boolean = true,
        @ColorRes backgroundRes: Int = R.color.bgPrimary
    ) = with(collector) {
        list.forEachIndexed { index, item ->
            commentItem {
                id("comment_${index}")
                item(item)
                formatter(dateTimeFormatter)
                displayReplyCount(displayReplyCount)
                backgroundRes(backgroundRes)
                avatarClickListener { onAvatarClick(it) }
                shareClickListener { onShareClick(it) }
                replyClickListener { onReplyClick(it) }
                favorClickListener { onFavorClick(it) }
                subCommentEntryClickListener { onSubCommentEntryClick(it) }
            }
        }
    }

    override fun onAvatarClick(uid: String) {
        userService.navigateToUserProfile(context, uid)
    }

    override fun onShareClick(item: Comment) {
        context.showToast("Click share")
    }

    override fun onReplyClick(item: Comment) {
        context.showToast("Click reply")
    }

    override fun onFavorClick(id: String) {
        context.showToast("Click favor")
    }

    override fun onSubCommentEntryClick(id: String) {
        CommentReplyActivity.start(context, id)
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, collector: ModelCollector): CommentControllerDelegate
    }
}