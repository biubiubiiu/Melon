package app.melon.feed.ui.widget

import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.NinePhotoView
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.FeedActions
import app.melon.feed.R
import app.melon.gallery.GalleryActivity
import app.melon.util.extensions.dpInt
import app.melon.util.formatter.MelonNumberFormatter
import app.melon.util.formatter.MelonDateTimeFormatter
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.lottie.LottieAnimationView


@EpoxyModelClass
abstract class AnonymousFeedItem : EpoxyModelWithHolder<AnonymousFeedItem.Holder>() {

    @EpoxyAttribute lateinit var actions: FeedActions

    @EpoxyAttribute lateinit var item: FeedAndAuthor
    @EpoxyAttribute lateinit var formatter: MelonDateTimeFormatter
    @EpoxyAttribute lateinit var numberFormatter: MelonNumberFormatter

    private val isFavor get() = item.feed.isFavored

    private val favorAnimator = ValueAnimator.ofFloat(0f, 0.5f)

    override fun getDefaultLayout(): Int = R.layout.item_anonymous_feed

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListeners(holder)
    }

    private fun setupContent(holder: Holder) {
        with(holder) {
            avatarView.load(R.drawable.ic_avatar_anonymous) {
                transformations(CircleCropTransformation())
            }
            usernameView.text = "Random name"
            userSchoolView.text = item.author.school
            postTimeView.text = formatter.formatShortRelativeTime(item.feed.postTime)

            titleView.isVisible = !item.feed.title.isNullOrBlank()
            titleView.text = item.feed.title

            contentView.text = item.feed.content
            commentView.text = numberFormatter.format(item.feed.replyCount)

            favorCount.text = numberFormatter.format(item.feed.favouriteCount)
            favorButton.progress = if (isFavor) 1f else 0f

            photoView.isVisible = item.feed.photos.isNotEmpty()
            photoView.takeIf { it.isVisible }?.apply {
                itemPadding(4.dpInt)
                cornerRadius(24f)
                urls = item.feed.photos
                onClickListener  { urls, index, _ ->
                    GalleryActivity.start(
                        context,
                        urls,
                        startPosition = index,
                        viewRefs = children.toList().filterIsInstance<ImageView>()
                    )
                }
                loadImage()
            }
        }
    }

    private fun setupListeners(holder: Holder) {
        with(holder) {
            containerView.setOnClickListener { actions.onHolderClick(item) }
            shareView.setOnClickListener {actions.onShareClick(item.feed) }
            commentView.setOnClickListener { actions.onCommentClick(item) }
            favorContainer.setOnClickListener {
                playFavorAnimation(isFavor)
                actions.onFavorClick(item.feed.id)
            }
            moreOperationView.setOnClickListener { actions.onMoreClick(item.feed) }
        }
    }

    private fun Holder.playFavorAnimation(isFavor: Boolean) {
        favorAnimator.cancel()
        favorAnimator.addUpdateListener {
            var progress = (it.animatedValue as Float)
            if (isFavor) {
                progress += 0.5f
            }
            favorButton.progress = progress
            Log.d("raymond", "progress is $progress")
        }
        favorAnimator.start()
    }

    class Holder : BaseEpoxyHolder() {
        internal val containerView: ViewGroup by bind(R.id.feed_container)
        internal val avatarView: ImageView by bind(R.id.feed_user_avatar)
        internal val usernameView: TextView by bind(R.id.feed_username)
        internal val userSchoolView: TextView by bind(R.id.feed_user_school)
        internal val postTimeView: TextView by bind(R.id.feed_post_time)
        internal val titleView: TextView by bind(R.id.feed_title)
        internal val contentView: TextView by bind(R.id.feed_content)
        internal val photoView: NinePhotoView by bind(R.id.feed_photos)
        internal val shareView: TextView by bind(R.id.feed_share)
        internal val commentView: TextView by bind(R.id.feed_comment)
        internal val favorContainer: View by bind(R.id.favor_container)
        internal val favorButton: LottieAnimationView by bind(R.id.feed_favor_button)
        internal val favorCount: TextView by bind(R.id.feed_favorite)
        internal val moreOperationView: ImageView by bind(R.id.feed_more)
    }
}