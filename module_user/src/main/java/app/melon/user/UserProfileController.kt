package app.melon.user

import android.content.Context
import app.melon.base.framework.BasePagingController
import app.melon.base.ui.list.divider
import app.melon.base.ui.list.vertSpaceConventional
import app.melon.base.ui.list.vertSpaceSmall
import app.melon.base.ui.ninePhotoView
import app.melon.base.ui.textHeader
import app.melon.base.utils.dpInt
import app.melon.base.utils.getResourceColor
import app.melon.base.utils.showToast
import app.melon.base.utils.sp
import app.melon.data.entities.Feed
import app.melon.data.entities.User
import app.melon.extensions.observable
import app.melon.user.ui.schoolInfo
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.group

class UserProfileController(
    context: Context
) : BasePagingController<Feed>(context) {

    var callbacks: Actions? by observable(null, ::requestModelBuild)
//    var user: User? by observable(null, ::requestModelBuild)

    var user: User? = null
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildItemModel(currentPosition: Int, item: Feed?): EpoxyModel<*> =
        ProfileFeedItem_()
            .id("feed ${item!!.feedId}")
            .item(item)
            .holderClickListener { callbacks?.onHolderClick() }
            .avatarClickListener { callbacks?.onAvatarClick() }
            .shareClickListener { callbacks?.onShareClick() }
            .commentClickListener { callbacks?.onCommentClick() }
            .favorClickListener { callbacks?.onFavorClick() }
            .moreClickListener { callbacks?.onMoreClick() }

    override fun addExtraModels() {
        val userNotNull = user ?: return
        userNotNull.photos.takeIf { it.isNotEmpty() }?.let {
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
                    padding(intArrayOf(12.dpInt, 0))
                    onClickListener { urls, index -> context.showToast("Click item $index, urls size: ${urls.size}") }
                    cornerRadius(24f)
                    urls(it)
                }
                vertSpaceSmall {
                    id("profile_photos_bottom_space")
                }
                divider {
                    id("profile_photos_bottom_divider")
                }
            }
        }
        schoolInfo {
            id("school_info")
            context(context)
            user(userNotNull)
        }
        vertSpaceConventional {
            id("profile_space_2")
        }
    }

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