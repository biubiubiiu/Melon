package app.melon.home.following.ui

import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import app.melon.base.framework.BaseEpoxyHolder
import app.melon.data.entities.Feed
import app.melon.home.following.R
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class FollowingFeedItem : EpoxyModelWithHolder<FollowingFeedItem.Holder>() {

    @EpoxyAttribute lateinit var holderClickListener: () -> Unit
    @EpoxyAttribute lateinit var avatarClickListener: () -> Unit
    @EpoxyAttribute lateinit var shareClickListener: () -> Unit
    @EpoxyAttribute lateinit var commentClickListener: () -> Unit
    @EpoxyAttribute lateinit var favorClickListener: () -> Unit
    @EpoxyAttribute lateinit var moreClickListener: () -> Unit

    @EpoxyAttribute lateinit var item: Feed

    override fun getDefaultLayout(): Int = R.layout.item_follow_feed

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListeners(holder)
    }

    private fun setupListeners(holder: Holder) {
        with(holder) {
            containerView.setOnClickListener { holderClickListener.invoke() }
            avatarView.setOnClickListener { avatarClickListener.invoke() }
            shareView.setOnClickListener { shareClickListener.invoke() }
            commentView.setOnClickListener { commentClickListener.invoke() }
            favoriteView.setOnClickListener { favorClickListener.invoke() }
            moreOperationView.setOnClickListener { moreClickListener.invoke() }
        }
    }

    private fun setupContent(holder: Holder) {
        with(holder) {
            avatarView.load(item.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            usernameView.text = item.username
            userIdView.text = item.userId
            userSchoolView.text = item.school
            postTimeView.text = item.postTime.toString()
            contentView.text = item.content
            commentView.text = item.comment.toString()
            favoriteView.text = item.favor.toString()
        }
    }

    class Holder : BaseEpoxyHolder() {
        val containerView by bind<ViewGroup>(R.id.feed_container)
        val avatarView by bind<ImageView>(R.id.feed_user_avatar)
        val usernameView by bind<TextView>(R.id.feed_username)
        val userIdView by bind<TextView>(R.id.feed_user_id)
        val userSchoolView by bind<TextView>(R.id.feed_user_school)
        val postTimeView by bind<TextView>(R.id.feed_post_time)
        val contentView by bind<TextView>(R.id.feed_content)
        val locationTagStub by bind<ViewStub>(R.id.feed_location_tag_stub)
        val typeTagStub by bind<ViewStub>(R.id.feed_type_tag_stub)
        val shareView by bind<TextView>(R.id.feed_share)
        val commentView by bind<TextView>(R.id.feed_comment)
        val favoriteView by bind<TextView>(R.id.feed_favorite)
        val moreOperationView by bind<ImageView>(R.id.feed_more)
    }
}