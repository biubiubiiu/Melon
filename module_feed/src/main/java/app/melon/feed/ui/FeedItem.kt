package app.melon.feed.ui

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.ImageViewerOverlay
import app.melon.base.ui.ShapedFourPhotoView
import app.melon.base.ui.TagView
import app.melon.data.entities.Feed
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.R
import app.melon.util.extensions.dpInt
import app.melon.util.number.MelonNumberFormatter
import app.melon.util.time.MelonDateTimeFormatter
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.stfalcon.imageviewer.StfalconImageViewer
import com.stfalcon.imageviewer.loader.ImageLoader


@EpoxyModelClass
abstract class FeedItem : EpoxyModelWithHolder<FeedItem.Holder>() {

    @EpoxyAttribute lateinit var holderClickListener: (FeedAndAuthor) -> Unit
    @EpoxyAttribute lateinit var avatarClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var shareClickListener: (Feed) -> Unit
    @EpoxyAttribute lateinit var commentClickListener: (Feed) -> Unit
    @EpoxyAttribute lateinit var favorClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var moreClickListener: (Feed) -> Unit
    @EpoxyAttribute lateinit var saveImageListener: (String) -> Unit

    @EpoxyAttribute lateinit var item: FeedAndAuthor
    @EpoxyAttribute lateinit var formatter: MelonDateTimeFormatter
    @EpoxyAttribute lateinit var numberFormatter: MelonNumberFormatter

    private var overlayView: ImageViewerOverlay? = null

    override fun getDefaultLayout(): Int = R.layout.item_feed

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListeners(holder)
    }

    private fun setupListeners(holder: Holder) {
        with(holder) {
            containerView.setOnClickListener { holderClickListener.invoke(item) }
            avatarView.setOnClickListener { avatarClickListener.invoke(item.author.id) }
            shareView.setOnClickListener { shareClickListener.invoke(item.feed) }
            commentView.setOnClickListener { commentClickListener.invoke(item.feed) }
            favoriteView.setOnClickListener { favorClickListener.invoke(item.feed.id) }
            moreOperationView.setOnClickListener { moreClickListener.invoke(item.feed) }
        }
    }

    private fun setupContent(holder: Holder) {
        with(holder) {
            avatarView.load(item.author.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            usernameView.text = item.author.username
            userIdView.text = "TODO"
            userSchoolView.text = item.author.school
            postTimeView.text = formatter.formatShortRelativeTime(item.feed.postTime)
            contentView.text = item.feed.content
            commentView.text = numberFormatter.format(item.feed.replyCount)
            favoriteView.text = numberFormatter.format(item.feed.favouriteCount)

            photoView.isVisible = item.feed.photos.isNotEmpty()
            photoView.takeIf { it.isVisible }?.apply {
                itemPadding = 4.dpInt
                cornerRadius = 32f
                urls = item.feed.photos
                photoView.loadImage()
                onClickListener = { urls, index, view ->
                    val loader = ImageLoader<String> { imageView, url -> imageView.load(url) }
                    StfalconImageViewer.Builder<String>(context, urls, loader)
                        .withStartPosition(index)
                        .withTransitionFrom(view)
                        .withHiddenStatusBar(false)
                        .withOverlayView(setupOverlayView(view.context, urls[index]))
                        .withImageChangeListener { position ->
                            overlayView?.update(urls[position])
                        }
                        .withDismissListener { overlayView = null }
                        .withBackgroundColorResource(R.color.bgSecondary)
                        .show()
                }
            }
        }
    }

    private fun setupOverlayView(context: Context, url: String): ImageViewerOverlay {
        if (overlayView == null) {
            overlayView = ImageViewerOverlay(context).apply {
                update(url)

                onSaveClick = { saveImageListener(it) }
            }
        }
        return overlayView!!
    }

    class Holder : BaseEpoxyHolder() {
        internal val containerView: ViewGroup by bind(R.id.feed_container)
        internal val avatarView: ImageView by bind(R.id.feed_user_avatar)
        internal val usernameView: TextView by bind(R.id.feed_username)
        internal val userIdView: TextView by bind(R.id.feed_user_id)
        internal val userSchoolView: TextView by bind(R.id.feed_user_school)
        internal val postTimeView: TextView by bind(R.id.feed_post_time)
        internal val contentView: TextView by bind(R.id.feed_content)
        internal val photoView: ShapedFourPhotoView by bind(R.id.feed_photos)
        internal val locationTag: TagView by bind(R.id.feed_location_tag)
        internal val typeTag: TagView by bind(R.id.feed_type_tag)
        internal val shareView: TextView by bind(R.id.feed_share)
        internal val commentView: TextView by bind(R.id.feed_comment)
        internal val favoriteView: TextView by bind(R.id.feed_favorite)
        internal val moreOperationView: ImageView by bind(R.id.feed_more)
    }
}