package app.melon.feed.ui

import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.NinePhotoView
import app.melon.data.entities.Feed
import app.melon.feed.R
import app.melon.util.extensions.dpInt
import app.melon.util.extensions.showToast
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class AnonymousFeedItem : EpoxyModelWithHolder<AnonymousFeedItem.Holder>() {

    @EpoxyAttribute lateinit var holderClickListener: () -> Unit
    @EpoxyAttribute lateinit var shareClickListener: () -> Unit
    @EpoxyAttribute lateinit var commentClickListener: () -> Unit
    @EpoxyAttribute lateinit var favorClickListener: () -> Unit
    @EpoxyAttribute lateinit var moreClickListener: () -> Unit

    @EpoxyAttribute lateinit var item: Feed

    override fun getDefaultLayout(): Int = R.layout.item_anonymous_feed

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListeners(holder)
    }

    private fun setupContent(holder: Holder) {
        with(holder) {
            avatarView.load(item.avatarUrl) {
                transformations(CircleCropTransformation()) // TODO add interceptor here
            }
            usernameView.text = item.username
            userIdView.text = item.userId
            userSchoolView.text = item.school
            postTimeView.text = item.postTime.toString()
            titleView.text = item.title
            contentView.text = item.content
            commentView.text = item.replyCount.toString()
            favoriteView.text = item.favouriteCount.toString()

            photoView.isVisible = item.photos.isNotEmpty()
            photoView.takeIf { it.isVisible }?.apply {
                itemPadding = 4.dpInt
                cornerRadius = 24f
                urls = item.photos
                photoView.loadImage()
                onClickListener = { urls, index -> context.showToast("Click item $index, urls size: ${urls.size}") }
            }
        }
    }

    private fun setupListeners(holder: Holder) {
        with(holder) {
            containerView.setOnClickListener { holderClickListener.invoke() }
            shareView.setOnClickListener { shareClickListener.invoke() }
            commentView.setOnClickListener { commentClickListener.invoke() }
            favoriteView.setOnClickListener { favorClickListener.invoke() }
            moreOperationView.setOnClickListener { moreClickListener.invoke() }
        }
    }

    class Holder : BaseEpoxyHolder() {
        val containerView by bind<ViewGroup>(R.id.feed_container)
        val avatarView by bind<ImageView>(R.id.feed_user_avatar)
        val usernameView by bind<TextView>(R.id.feed_username)
        val userIdView by bind<TextView>(R.id.feed_user_id)
        val userSchoolView by bind<TextView>(R.id.feed_user_school)
        val postTimeView by bind<TextView>(R.id.feed_post_time)
        val titleView by bind<TextView>(R.id.feed_title)
        val contentView by bind<TextView>(R.id.feed_content)
        val photoView by bind<NinePhotoView>(R.id.feed_photos)
        val locationTagStub by bind<ViewStub>(R.id.feed_location_tag_stub)
        val typeTagStub by bind<ViewStub>(R.id.feed_type_tag_stub)
        val shareView by bind<TextView>(R.id.feed_share)
        val commentView by bind<TextView>(R.id.feed_comment)
        val favoriteView by bind<TextView>(R.id.feed_favorite)
        val moreOperationView by bind<ImageView>(R.id.feed_more)
    }
}