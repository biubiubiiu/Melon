package app.melon.user.ui.detail

import android.content.Context
import android.view.View
import app.melon.base.ui.list.divider
import app.melon.base.ui.list.refreshView
import app.melon.base.ui.list.vertSpaceSmall
import app.melon.base.ui.ninePhotoView
import app.melon.base.ui.textHeader
import app.melon.util.extensions.dpInt
import app.melon.util.extensions.getResourceColor
import app.melon.util.extensions.showToast
import app.melon.util.extensions.sp
import app.melon.data.entities.User
import app.melon.data.resultentities.FeedAndAuthor
import app.melon.feed.ui.controller.FeedControllerDelegate
import app.melon.user.R
import app.melon.user.ui.widget.schoolInfo
import com.airbnb.epoxy.Typed3EpoxyController
import com.airbnb.epoxy.group
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class UserProfileController @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val action: Action,
    private val factory: FeedControllerDelegate.Factory
) : Typed3EpoxyController<User, List<FeedAndAuthor>, Boolean>() {

    private val delegate by lazy {
        factory.create(context)
    }

    override fun buildModels(user: User?, feeds: List<FeedAndAuthor>?, refreshing: Boolean) {
        if (refreshing) {
            refreshView {
                id("user_profile_refresh_view")
            }
            return
        }
        if (user != null) {
            user.photos.takeIf { it.isNotEmpty() }?.let {
                buildPhotos(it)
            }
            buildSchoolInfo(user)
        }
        buildFeeds(feeds)
        if (feeds?.size == 5) {
            buildLoadMoreButton()
        }
    }

    private fun buildPhotos(urls: List<String>) {
        group {
            id("profile_photos")
            layout(R.layout.layout_vertical_linear_group)
            textHeader {
                id("profile_photos_header")
                content(R.string.user_profile_photos)
                color(getResourceColor(R.color.TextPrimary))
                textSize(15.sp)
                padding(intArrayOf(12.dpInt, 12.dpInt))
                background(R.color.bgPrimary)
            }
            ninePhotoView {
                id("profile_photos")
                itemPadding(4.dpInt)
                background(R.color.bgPrimary)
                paddingHorizontal(12.dpInt)
                whRatio(1f)
                onClickListener { urls, index -> context.showToast("Click item $index, urls size: ${urls.size}") }
                cornerRadius(24f)
                urls(urls)
            }
            vertSpaceSmall {
                id("profile_photos_bottom_space")
            }
            divider {
                id("profile_photos_bottom_divider")
            }
        }
    }

    private fun buildSchoolInfo(user: User) {
        schoolInfo {
            id("school_info")
            context(context)
            user(user)
        }
        vertSpaceSmall {
            id("profile_info_bottom_space")
        }
        textHeader {
            id("profile_feeds_header")
            content(R.string.user_profile_posts)
            color(getResourceColor(R.color.TextPrimary))
            textSize(15.sp)
            padding(intArrayOf(12.dpInt, 12.dpInt))
            background(R.color.bgPrimary)
        }
    }

    private fun buildFeeds(items: List<FeedAndAuthor>?) {
        items?.forEachIndexed { index, item ->
            add(delegate.buildFeedItem(
                dataProvider = { item },
                idProvider = { "user_profile_feed_$index" }
            ))
        }
    }

    private fun buildLoadMoreButton() {
        divider {
            id("profile_show_more_divider")
        }
        textHeader {
            id("profile_load_more_posts")
            content(R.string.user_profile_show_more)
            color(getResourceColor(R.color.colorPrimary))
            textSize(15.sp)
            padding(intArrayOf(12.dpInt, 16.dpInt))
            background(R.color.bgPrimary)
            transitionName("profile_load_more_posts")
            onClickListener { action.onShowMoreButtonClick(it) }
        }
    }

    interface Action {
        fun onShowMoreButtonClick(view: View)
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, action: Action): UserProfileController
    }
}