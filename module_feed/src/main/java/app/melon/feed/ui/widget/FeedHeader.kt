package app.melon.feed.ui.widget

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.ShapedFourPhotoView
import app.melon.base.ui.TagView
import app.melon.data.entities.Feed
import app.melon.data.entities.PoiInfo
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.R
import app.melon.gallery.GalleryActivity
import app.melon.location.LocationHelper
import app.melon.util.extensions.dpInt
import app.melon.util.formatter.MelonDateTimeFormatter
import app.melon.util.formatter.MelonDistanceFormatter
import app.melon.util.formatter.MelonNumberFormatter
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
    @EpoxyAttribute lateinit var shareClickListener: (Feed) -> Unit
    @EpoxyAttribute lateinit var moreClickListener: (String) -> Unit
    @EpoxyAttribute lateinit var poiEntryClickListener: (PoiInfo) -> Unit

    @EpoxyAttribute lateinit var item: FeedAndAuthor
    @EpoxyAttribute lateinit var locationHelper: LocationHelper
    @EpoxyAttribute lateinit var formatter: MelonDateTimeFormatter
    @EpoxyAttribute lateinit var numberFormatter: MelonNumberFormatter
    @EpoxyAttribute lateinit var distanceFormatter: MelonDistanceFormatter

    override fun getDefaultLayout(): Int = R.layout.view_feed_detail_header

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListeners(holder)
    }

    private fun setupContent(holder: Holder) = with(holder) {
        avatarView.setOnClickListener { avatarClickListener.invoke(item.author.id) }
        shareView.setOnClickListener { shareClickListener.invoke(item.feed) }
        commentView.setOnClickListener { commentClickListener.invoke(item.feed.id) }
        favoriteView.setOnClickListener { favorClickListener.invoke(item.feed.id) }
        moreOperationView.setOnClickListener { moreClickListener.invoke(item.feed.id) }
        repostCountView.setOnClickListener { repostEntryClickListener.invoke(item.feed.id) }
        repostLabel.setOnClickListener { repostEntryClickListener.invoke(item.feed.id) }
        favorCountView.setOnClickListener { favorEntryClickListener.invoke(item.feed.id) }
        favorLabel.setOnClickListener { favorEntryClickListener.invoke(item.feed.id) }
        locationTag.setOnClickListener {
            item.feed.poiInfo?.let {
                poiEntryClickListener.invoke(it)
            }
        }
    }

    private fun setupListeners(holder: Holder) = with(holder) {
        avatarView.load(item.author.avatarUrl) {
            transformations(CircleCropTransformation())
        }
        usernameView.text = item.author.username
        userIdView.text = "TODO"
        userSchoolView.text = item.author.school
        postTimeView.text = formatter.formatMediumDateTime(item.feed.postTime)
        contentView.text = item.feed.content

        val favorCount = item.feed.favouriteCount
        if (favorCount != null && favorCount > 0) {
            favorCountView.isVisible = true
            favorLabel.isVisible = true
            favorCountView.text = numberFormatter.format(favorCount)
        } else {
            favorCountView.isVisible = false
            favorLabel.isVisible = false
        }

        val repostCount = item.feed.repostCount
        if (repostCount != null && repostCount > 0) {
            repostCountView.isVisible = true
            repostLabel.isVisible = true
            repostCountView.text = numberFormatter.format(repostCount)
        } else {
            repostCountView.isVisible = false
            repostLabel.isVisible = false
        }

        photoView.isVisible = item.feed.photos.isNotEmpty()
        photoView.takeIf { it.isVisible }?.apply {
            itemPadding(4.dpInt)
            cornerRadius(32f)
            urls = item.feed.photos
            onClickListener { urls, index, _ ->
                GalleryActivity.start(
                    context,
                    urls,
                    startPosition = index,
                    viewRefs = children.toList().filterIsInstance<ImageView>()
                )
            }
            loadImage()
        }

        val poiInfo = item.feed.poiInfo
        locationTag.isVisible = poiInfo != null
        if (poiInfo != null) {
            val distance = locationHelper.getMyDistanceTo(poiInfo.longitude, poiInfo.latitude)
            locationTag.render(
                TagView.TagStyle.Poi(
                    poiInfo.poiName,
                    distanceFormatter.formatDistance(distance)
                )
            )
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
        internal val shareView: View by bind(R.id.feed_share)
        internal val commentView: View by bind(R.id.feed_comment)
        internal val favoriteView: View by bind(R.id.feed_favorite)
        internal val moreOperationView: View by bind(R.id.feed_more)
        internal val repostCountView: TextView by bind(R.id.feed_repost_count)
        internal val repostLabel: TextView by bind(R.id.feed_repost_label)
        internal val favorCountView: TextView by bind(R.id.feed_like_count)
        internal val favorLabel: TextView by bind(R.id.feed_like_label)
    }
}