package app.melon.comment.ui

import android.content.Context
import android.graphics.Typeface
import app.melon.account.api.UserManager
import app.melon.base.framework.BasePagingController
import app.melon.base.ui.extensions.activityContext
import app.melon.base.ui.textHeader
import app.melon.comment.CommentControllerDelegate
import app.melon.comment.ICommentService
import app.melon.comment.R
import app.melon.comment.ReplyActions
import app.melon.comment.ui.widget.ReplyItem_
import app.melon.composer.api.ComposerEntry
import app.melon.composer.api.Reply
import app.melon.data.entities.Comment

import app.melon.data.resultentities.CommentAndAuthor
import app.melon.user.api.IUserService
import app.melon.util.extensions.dpInt
import app.melon.util.extensions.getColorCompat
import app.melon.util.extensions.showToast
import app.melon.util.extensions.sp
import app.melon.util.formatter.MelonDateTimeFormatter
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


internal class ReplyPageController @AssistedInject constructor(
    @Assisted context: Context,
    factory: CommentControllerDelegate.Factory,
    private val userService: IUserService,
    private val commentService: ICommentService,
    private val userManager: UserManager,
    private val dateTimeFormatter: MelonDateTimeFormatter
) : ReplyActions, BasePagingController<CommentAndAuthor>(
    context,
    sameItemIndicator = { oldItem, newItem -> oldItem.comment.id == newItem.comment.id }
) {

    private val delegate = factory.create(context, this)

    internal var firstComment: CommentAndAuthor? = null
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildItemModel(currentPosition: Int, item: CommentAndAuthor?): EpoxyModel<*> {
        return ReplyItem_()
            .id("reply_$currentPosition")
            .item(item!!)
            .formatter(dateTimeFormatter)
            .favorClickListener { onFavorClick(it) }
            .shareClickListener { onShareClick(it) }
            .replyEntryClickListener { context.showToast("click reply") }
            .profileEntryClickListener { onProfileEntryClick(it) }
    }

    override fun addExtraModels() {
        delegate?.buildCommentList(
            listOf(firstComment),
            displayReplyCount = false,
            backgroundRes = R.color.bgSecondary
        )
        textHeader {
            id("all_replies_header")
            content(R.string.comment_all_replies)
            textSize(14.sp)
            typeface(Typeface.BOLD)
            color(context.getColorCompat(R.color.TextPrimary))
            padding(intArrayOf(12.dpInt, 16.dpInt))
            background(R.color.bgSecondary)
        }
    }

    @AssistedFactory
    internal interface Factory {
        fun create(context: Context): ReplyPageController
    }

    override fun onShareClick(item: Comment) {
        context.showToast("click share")
    }

    override fun onReplyClick(item: Comment) {
        val user = userManager.user ?: return
        (context.activityContext as? ComposerEntry)?.launchComposer(
            option = Reply(
                accountAvatarUrl = user.avatarUrl
            ),
            callback = { result ->
                val (content, _, _) = result ?: return@launchComposer
                commentService.postReply(context, item.id, content)
            }
        )
    }

    override fun onFavorClick(id: String) {
        context.showToast("click favor")
    }

    override fun onProfileEntryClick(id: String) {
        userService.navigateToUserProfile(context, id)
    }
}