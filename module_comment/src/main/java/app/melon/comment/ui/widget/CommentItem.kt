package app.melon.comment.ui.widget

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.view.isVisible
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.comment.R
import app.melon.data.resultentities.CommentAndAuthor
import app.melon.util.extensions.getResourceString
import app.melon.util.formatter.MelonDateTimeFormatter
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass
abstract class CommentItem : EpoxyModelWithHolder<CommentItem.Holder>() {

    @EpoxyAttribute lateinit var avatarClickListener: () -> Unit
    @EpoxyAttribute lateinit var shareClickListener: () -> Unit
    @EpoxyAttribute lateinit var replyClickListener: () -> Unit
    @EpoxyAttribute lateinit var favorClickListener: () -> Unit
    @EpoxyAttribute lateinit var subCommentEntryClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var userEntryClickListener: (String) -> Unit

    @EpoxyAttribute lateinit var item: CommentAndAuthor
    @EpoxyAttribute lateinit var formatter: MelonDateTimeFormatter

    @EpoxyAttribute var displayReplyCount: Boolean = true
    @EpoxyAttribute @ColorRes var backgroundRes: Int = R.color.bgPrimary

    override fun getDefaultLayout(): Int = R.layout.item_comment

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListeners(holder)
    }

    private fun setupContent(holder: Holder) = with(holder) {
            containerView.setBackgroundResource(backgroundRes)
            avatarView.load(item.author.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            usernameView.text = item.author.username.orEmpty()
            userIdView.text = "TODO"
            postTimeView.text = formatter.formatShortRelativeTime(item.comment.postTime)
            contentView.text = item.comment.content.orEmpty()
            favourCountView.text = item.comment.favorCount?.toString()

            val replyCount = item.comment.replyCount
            if (replyCount != null && replyCount > 0) {
                moreReplyEntryView.isVisible = true
                moreReplyEntryView.text = buildReplyEntryClickableSpan(replyCount)
                moreReplyEntryView.movementMethod = LinkMovementMethod.getInstance()
            } else {
                moreReplyEntryView.isVisible = false
            }
    }

    private fun setupListeners(holder: Holder) {
        with(holder) {
            avatarView.setOnClickListener { avatarClickListener.invoke() }
            shareView.setOnClickListener { shareClickListener.invoke() }
            replyView.setOnClickListener { replyClickListener.invoke() }
            favoriteView.setOnClickListener { favorClickListener.invoke() }
        }
    }

    private fun buildReplyEntryClickableSpan(count: Long): SpannableString {
        val ss = SpannableString(getResourceString(R.string.comment_total_reply_count, count))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                subCommentEntryClickListener.invoke(item.comment.id)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(clickableSpan, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    class Holder : BaseEpoxyHolder() {
        internal val containerView: View by bind(R.id.comment_container)
        internal val avatarView: ImageView by bind(R.id.comment_user_avatar)
        internal val usernameView: TextView by bind(R.id.comment_username)
        internal val userIdView: TextView by bind(R.id.comment_user_id)
        internal val postTimeView: TextView by bind(R.id.comment_post_time)
        internal val contentView: TextView by bind(R.id.comment_content)
        internal val shareView: ImageView by bind(R.id.comment_share)
        internal val replyView: ImageView by bind(R.id.comment_reply)
        internal val favoriteView: ImageView by bind(R.id.comment_favourite)
        internal val favourCountView: TextView by bind(R.id.comment_favor_count)
        internal val moreReplyEntryView: TextView by bind(R.id.more_reply_entry)
    }
}