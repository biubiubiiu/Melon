package app.melon.comment.ui.widget

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
import app.melon.comment.R
import app.melon.data.resultentities.CommentAndAuthor
import app.melon.util.extensions.toOffsetDateTime
import app.melon.util.formatter.MelonDateTimeFormatter
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass
abstract class ReplyItem : EpoxyModelWithHolder<ReplyItem.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.item_reply

    @EpoxyAttribute lateinit var favorClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var shareClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var replyEntryClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var profileEntryClickListener: (String) -> Unit

    @EpoxyAttribute lateinit var item: CommentAndAuthor
    @EpoxyAttribute lateinit var formatter: MelonDateTimeFormatter

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListeners(holder)
    }

    private fun setupContent(holder: Holder) {
        with(holder) {
            avatarView.load(item.author.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            usernameView.text = item.author.username
            userIdView.text = item.author.id
            postTimeView.text = formatter.formatShortRelativeTime(item.comment.postTime.toOffsetDateTime())
            contentView.text = item.comment.content
            favourCountView.text = item.comment.favorCount.toString()

            val quote = item.quote
            quoteContainer.isVisible = quote != null
            quoteContainer.takeIf { it.isVisible }?.run {
                quoteContent.text = buildReplyClickableSpan(quote!!.author.username, quote.comment.content)
                quoteContent.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    private fun setupListeners(holder: Holder) {
        with(holder) {
            avatarView.setOnClickListener { profileEntryClickListener.invoke(item.author.id) }
            shareView.setOnClickListener { shareClickListener.invoke(item.comment.id) }
            replyView.setOnClickListener { replyEntryClickListener.invoke(item.comment.id) }
            favoriteView.setOnClickListener { favorClickListener.invoke(item.comment.id) }
        }
    }

    private fun buildReplyClickableSpan(username: String, content: String): SpannableString {
        val ss = SpannableString("$username: $content")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                profileEntryClickListener.invoke(item.quote!!.author.id)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(clickableSpan, 0, username.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
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
        internal val quoteContainer: View by bind(R.id.quote_container)
        internal val quoteContent: TextView by bind(R.id.quote_content)
    }
}