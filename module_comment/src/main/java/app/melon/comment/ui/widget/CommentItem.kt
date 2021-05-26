package app.melon.comment.ui.widget

import android.annotation.SuppressLint
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
import app.melon.base.ui.extensions.makeGone
import app.melon.base.ui.extensions.makeVisible
import app.melon.comment.R
import app.melon.data.entities.Comment
import app.melon.data.resultentities.CommentAndAuthor
import app.melon.util.extensions.getResourceString
import app.melon.util.formatter.MelonDateTimeFormatter
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.facebook.shimmer.ShimmerFrameLayout


@EpoxyModelClass
internal abstract class CommentItem : EpoxyModelWithHolder<CommentItem.Holder>() {

    @EpoxyAttribute lateinit var avatarClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var shareClickListener: (Comment) -> Unit
    @EpoxyAttribute lateinit var replyClickListener: (Comment) -> Unit
    @EpoxyAttribute lateinit var favorClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var subCommentEntryClickListener: (String) -> Unit

    @EpoxyAttribute var item: CommentAndAuthor? = null
    @EpoxyAttribute lateinit var formatter: MelonDateTimeFormatter

    @EpoxyAttribute var displayReplyCount: Boolean = true
    @EpoxyAttribute @ColorRes var backgroundRes: Int = R.color.bgPrimary

    override fun getDefaultLayout(): Int = R.layout.item_comment

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListeners(holder)
    }

    @SuppressLint("SetTextI18n")
    private fun setupContent(holder: Holder) = with(holder) {
        contentRoot.setBackgroundResource(backgroundRes)
        val data = item
        if (data != null) {
            shimmerContainer.stopShimmer()
            shimmerContainer.makeGone()
            contentRoot.makeVisible()
            avatarView.load(data.author.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            usernameView.text = data.author.username.orEmpty()
            userIdView.text = "@${data.author.customId}"
            postTimeView.text = formatter.formatShortRelativeTime(data.comment.postTime)
            contentView.text = data.comment.content.orEmpty()
            favourCountView.text = data.comment.favorCount?.toString()

            val replyCount = data.comment.replyCount
            if (replyCount != null && replyCount > 0 && displayReplyCount) {
                moreReplyEntryView.isVisible = true
                moreReplyEntryView.text = buildReplyEntryClickableSpan(data)
                moreReplyEntryView.movementMethod = LinkMovementMethod.getInstance()
            } else {
                moreReplyEntryView.isVisible = false
            }
        } else {
            contentRoot.makeGone()
            shimmerContainer.makeVisible()
            shimmerContainer.startShimmer()
        }
    }

    private fun setupListeners(holder: Holder) = with(holder) {
        item?.let { data ->
            avatarView.setOnClickListener { avatarClickListener.invoke(data.author.id) }
            shareView.setOnClickListener { shareClickListener.invoke(data.comment) }
            replyView.setOnClickListener { replyClickListener.invoke(data.comment) }
            favoriteView.setOnClickListener { favorClickListener.invoke(data.comment.id) }
        }
    }

    private fun buildReplyEntryClickableSpan(item: CommentAndAuthor): SpannableString {
        val replyCount = item.comment.replyCount
        val ss = SpannableString(getResourceString(R.string.comment_total_reply_count, replyCount))
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

    internal class Holder : BaseEpoxyHolder() {
        internal val shimmerContainer: ShimmerFrameLayout by bind(R.id.shimmer_container)
        internal val contentRoot: View by bind(R.id.content_root)
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