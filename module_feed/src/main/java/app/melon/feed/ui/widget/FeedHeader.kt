package app.melon.feed.ui.widget

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.ShapedFourPhotoView
import app.melon.base.uikit.TagView
import app.melon.data.entities.Feed
import app.melon.feed.R
import app.melon.util.extensions.dpInt
import app.melon.util.extensions.toOffsetDateTime
import app.melon.util.time.MelonDateTimeFormatter
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass
abstract class FeedHeader : EpoxyModelWithHolder<FeedHeader.Holder>() {

    @EpoxyAttribute lateinit var avatarClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var repostEntryClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var favorEntryClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var commentClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var favorClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var shareClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var moreClickListener: (String) -> Unit

    @EpoxyAttribute lateinit var item: Feed
    @EpoxyAttribute lateinit var formatter: MelonDateTimeFormatter

    override fun getDefaultLayout(): Int = R.layout.view_feed_detail_header

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListeners(holder)
    }

    private fun setupContent(holder: Holder) {
        with(holder) {
            avatarView.setOnClickListener { avatarClickListener.invoke(item.userId) }
            shareView.setOnClickListener { shareClickListener.invoke(item.feedId) }
            commentView.setOnClickListener { commentClickListener.invoke(item.feedId) }
            favoriteView.setOnClickListener { favorClickListener.invoke(item.feedId) }
            moreOperationView.setOnClickListener { moreClickListener.invoke(item.feedId) }
            repostCountView.setOnClickListener { repostEntryClickListener.invoke(item.feedId) }
            repostLabel.setOnClickListener { repostEntryClickListener.invoke(item.feedId) }
            favorCountView.setOnClickListener { favorEntryClickListener.invoke(item.feedId) }
            favorLabel.setOnClickListener { favorEntryClickListener.invoke(item.feedId) }
        }
    }

    private fun setupListeners(holder: Holder) {
        with(holder) {
            avatarView.load(item.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            usernameView.text = item.username
            userIdView.text = item.userId
            userSchoolView.text = item.school
            postTimeView.text = formatter.formatShortRelativeTime(item.postTime.toOffsetDateTime())
            contentView.text = item.content
//            commentView.text = item.replyCount.toString()
            favorCountView.isVisible = item.favouriteCount > 0
            favorLabel.isVisible = item.favouriteCount > 0
            favorCountView.takeIf { it.isVisible }?.text = item.favouriteCount.toString()

            val repostCount = 2 // TODO use repost count
            repostCountView.isVisible = repostCount > 0
            repostLabel.isVisible = repostCount > 0
            repostCountView.takeIf { it.isVisible }?.text = repostCount.toString()

            photoView.isVisible = item.photos.isNotEmpty()
            photoView.takeIf { it.isVisible }?.apply {
                itemPadding = 4.dpInt
                cornerRadius = 32f
                urls = item.photos
                photoView.loadImage()
            }
        }
    }

    class Holder : BaseEpoxyHolder() {
        internal val avatarView: ImageView by bind(R.id.feed_user_avatar)
        internal val usernameView: TextView by bind(R.id.feed_username)
        internal val userIdView: TextView by bind(R.id.feed_user_id)
        internal val userSchoolView: TextView by bind(R.id.feed_user_school)
        internal val postTimeView: TextView by bind(R.id.feed_post_time)
        internal val contentView: TextView by bind(R.id.feed_content)
        internal val photoView: ShapedFourPhotoView by bind(R.id.feed_photos)
        internal val locationTag: TagView by bind(R.id.feed_location_tag)
        internal val typeTag: TagView by bind(R.id.feed_type_tag)
        internal val shareView: ImageView by bind(R.id.feed_share)
        internal val commentView: ImageView by bind(R.id.feed_comment)
        internal val favoriteView: ImageView by bind(R.id.feed_favorite)
        internal val moreOperationView: ImageView by bind(R.id.feed_more)
        internal val repostCountView: TextView by bind(R.id.feed_repost_count)
        internal val repostLabel: TextView by bind(R.id.feed_repost_label)
        internal val favorCountView: TextView by bind(R.id.feed_like_count)
        internal val favorLabel: TextView by bind(R.id.feed_like_label)
    }
}