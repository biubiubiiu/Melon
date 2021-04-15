package app.melon.feed.ui.widget

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.NinePhotoView
import app.melon.base.ui.TagView
import app.melon.data.entities.Feed
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.R
import app.melon.util.extensions.dpInt
import app.melon.util.extensions.showToast
import app.melon.util.number.MelonNumberFormatter
import app.melon.util.time.MelonDateTimeFormatter
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass
abstract class AnonymousFeedItem : EpoxyModelWithHolder<AnonymousFeedItem.Holder>() {

    @EpoxyAttribute lateinit var holderClickListener: (FeedAndAuthor) -> Unit
    @EpoxyAttribute lateinit var shareClickListener: (Feed) -> Unit
    @EpoxyAttribute lateinit var commentClickListener: (Feed) -> Unit
    @EpoxyAttribute lateinit var favorClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var moreClickListener: (Feed) -> Unit

    @EpoxyAttribute lateinit var item: FeedAndAuthor
    @EpoxyAttribute lateinit var formatter: MelonDateTimeFormatter
    @EpoxyAttribute lateinit var numberFormatter: MelonNumberFormatter

    override fun getDefaultLayout(): Int = R.layout.item_anonymous_feed

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListeners(holder)
    }

    private fun setupContent(holder: Holder) {
        with(holder) {
            avatarView.load(item.author.avatarUrl) {
                transformations(CircleCropTransformation()) // TODO add interceptor here
            }
            usernameView.text = item.author.username
            userIdView.text = "TODO"
            userSchoolView.text = item.author.school
            postTimeView.text = formatter.formatShortRelativeTime(item.feed.postTime)
            titleView.text = item.feed.title
            contentView.text = item.feed.content
            commentView.text = numberFormatter.format(item.feed.replyCount)
            favoriteView.text = numberFormatter.format(item.feed.favouriteCount)

            photoView.isVisible = item.feed.photos.isNotEmpty()
            photoView.takeIf { it.isVisible }?.apply {
                itemPadding = 4.dpInt
                cornerRadius = 24f
                urls = item.feed.photos
                photoView.loadImage()
                onClickListener = { urls, index -> context.showToast("Click item $index, urls size: ${urls.size}") }
            }
        }
    }

    private fun setupListeners(holder: Holder) {
        with(holder) {
            containerView.setOnClickListener { holderClickListener.invoke(item) }
            shareView.setOnClickListener { shareClickListener.invoke(item.feed) }
            commentView.setOnClickListener { commentClickListener.invoke(item.feed) }
            favoriteView.setOnClickListener { favorClickListener.invoke(item.feed.id) }
            moreOperationView.setOnClickListener { moreClickListener.invoke(item.feed) }
        }
    }

    class Holder : BaseEpoxyHolder() {
        internal val containerView: ViewGroup by bind(R.id.feed_container)
        internal val avatarView: ImageView by bind(R.id.feed_user_avatar)
        internal val usernameView: TextView by bind(R.id.feed_username)
        internal val userIdView: TextView by bind(R.id.feed_user_id)
        internal val userSchoolView: TextView by bind(R.id.feed_user_school)
        internal val postTimeView: TextView by bind(R.id.feed_post_time)
        internal val titleView: TextView by bind(R.id.feed_title)
        internal val contentView: TextView by bind(R.id.feed_content)
        internal val photoView: NinePhotoView by bind(R.id.feed_photos)
        internal val locationTag: TagView by bind(R.id.feed_location_tag)
        internal val typeTag: TagView by bind(R.id.feed_type_tag)
        internal val shareView: TextView by bind(R.id.feed_share)
        internal val commentView: TextView by bind(R.id.feed_comment)
        internal val favoriteView: TextView by bind(R.id.feed_favorite)
        internal val moreOperationView: ImageView by bind(R.id.feed_more)
    }
}