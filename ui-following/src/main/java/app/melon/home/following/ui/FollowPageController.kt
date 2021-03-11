package app.melon.home.following.ui

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.data.resultentities.FollowingEntryWithFeed
import app.melon.extensions.observable
import com.airbnb.epoxy.EpoxyModel


class FollowPageController(
    context: Context
) : BasePagingController<FollowingEntryWithFeed>(context) {

    var callbacks: Actions? by observable(null, ::requestModelBuild)

    override fun buildItemModel(currentPosition: Int, item: FollowingEntryWithFeed?): EpoxyModel<*> =
        FollowingFeedItem_()
            .id("feed ${item!!.entry.id}")
            .item(item.feed)
            .holderClickListener { callbacks?.onHolderClick() }
            .avatarClickListener { callbacks?.onAvatarClick() }
            .shareClickListener { callbacks?.onShareClick() }
            .commentClickListener { callbacks?.onCommentClick() }
            .favorClickListener { callbacks?.onFavorClick() }
            .moreClickListener { callbacks?.onMoreClick() }

    fun clear() {
        callbacks = null
    }

    interface Actions {
        fun onHolderClick()
        fun onAvatarClick()
        fun onShareClick()
        fun onCommentClick()
        fun onFavorClick()
        fun onMoreClick()
    }
}