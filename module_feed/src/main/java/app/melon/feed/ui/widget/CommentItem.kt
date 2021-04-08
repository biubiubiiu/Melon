package app.melon.feed.ui.widget

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.data.entities.Comment
import app.melon.feed.R
import app.melon.util.extensions.getResourceString
import app.melon.util.extensions.ifTrue
import app.melon.util.extensions.setVisibleIf
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
    @EpoxyAttribute lateinit var replyEntryClickListener: () -> Unit
    @EpoxyAttribute lateinit var userEntryClickListener: (String) -> Unit

    @EpoxyAttribute lateinit var item: Comment

    override fun getDefaultLayout(): Int = R.layout.item_comment

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListeners(holder)
    }

    private fun setupContent(holder: Holder) {
        with(holder) {
            avatarView.load(item.displayPoster.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            usernameView.text = item.displayPoster.username
            userIdView.text = item.displayPoster.id
            postTimeView.text = item.postTime.toString()
            contentView.text = item.content
            favourCountView.text = item.favorCount.toString()

            displayReplyContainer.isVisible = item.displayReply.isNotEmpty()
            displayReplyContainer.setVisibleIf(item.displayReply.isNotEmpty()).ifTrue {
                val firstReply = item.displayReply.getOrNull(0)
                if (firstReply != null) {
                    replyOne.isVisible = true
                    replyOne.text = buildReplyClickableSpan(firstReply.displayPoster.username, firstReply.content)
                    replyOne.movementMethod = LinkMovementMethod.getInstance()
                } else {
                    replyOne.isVisible = false
                }

                val secondReply = item.displayReply.getOrNull(1)
                if (secondReply != null) {
                    replyTwo.isVisible = true
                    replyTwo.text = buildReplyClickableSpan(secondReply.displayPoster.username, secondReply.content)
                    replyTwo.movementMethod = LinkMovementMethod.getInstance()
                } else {
                    replyTwo.isVisible = false
                }

                moreReplyEntryView.setVisibleIf(item.hasMoreReply)
                moreReplyEntryView.takeIf { it.isVisible }?.text = buildReplyEntryClickableSpan(item.replyCount)
            }
        }
    }

    private fun setupListeners(holder: Holder) {
        with(holder) {
            avatarView.setOnClickListener { avatarClickListener.invoke() }
            shareView.setOnClickListener { shareClickListener.invoke() }
            replyView.setOnClickListener { replyClickListener.invoke() }
            favoriteView.setOnClickListener { favorClickListener.invoke() }
            moreReplyEntryView.setOnClickListener { replyEntryClickListener.invoke() }
        }
    }

    private fun buildReplyClickableSpan(username: String, content: String): SpannableString {
        val ss = SpannableString("$username: $content")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                userEntryClickListener.invoke(item.displayPoster.id)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(clickableSpan, 0, username.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    private fun buildReplyEntryClickableSpan(count: Int): SpannableString {
        val ss = SpannableString(getResourceString(R.string.comment_total_reply_count, count))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                replyEntryClickListener.invoke()
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
        internal val displayReplyContainer: View by bind(R.id.reply_container)
        internal val replyOne: TextView by bind(R.id.reply_one)
        internal val replyTwo: TextView by bind(R.id.reply_two)
    }
}