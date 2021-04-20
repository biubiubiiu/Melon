package app.melon.comment.ui

import android.content.Context
import android.graphics.Typeface
import app.melon.base.framework.BasePagingController
import app.melon.base.ui.textHeader
import app.melon.comment.CommentControllerDelegate
import app.melon.comment.R
import app.melon.comment.ui.widget.ReplyItem_

import app.melon.data.resultentities.CommentAndAuthor
import app.melon.user.api.IUserService
import app.melon.util.delegates.observable
import app.melon.util.extensions.dpInt
import app.melon.util.extensions.getColorCompat
import app.melon.util.extensions.showToast
import app.melon.util.extensions.sp
import app.melon.util.formatter.MelonDateTimeFormatter
import com.airbnb.epoxy.EpoxyModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class ReplyPageController @AssistedInject constructor(
    @Assisted context: Context,
    factory: CommentControllerDelegate.Factory,
    private val userService: IUserService,
    private val dateTimeFormatter: MelonDateTimeFormatter
) : BasePagingController<CommentAndAuthor>(
    context,
    sameContentIndicator = { oldItem, newItem -> oldItem.comment.id == newItem.comment.id }
) {

    var loadingFirstComment: Boolean by observable(false, ::requestModelBuild)
    var firstComment: CommentAndAuthor? = null
        set(value) {
            field = value
            requestModelBuild()
        }

    private val delegate = factory.create(context, this)

    override fun buildItemModel(currentPosition: Int, item: CommentAndAuthor?): EpoxyModel<*> {
        return ReplyItem_()
            .id("reply_$currentPosition")
            .item(item!!)
            .formatter(dateTimeFormatter)
            .favorClickListener { context.showToast("click favor") }
            .shareClickListener { context.showToast("click share") }
            .replyEntryClickListener { context.showToast("click reply") }
            .profileEntryClickListener { userService.navigateToUserProfile(context, it) }
    }

    override fun addExtraModels() {
        delegate?.buildCommentList(
            firstComment?.let { listOf(it) },
            loading = loadingFirstComment,
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
    interface Factory {
        fun create(context: Context): ReplyPageController
    }
}