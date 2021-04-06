package app.melon.user.ui.edit

import android.widget.Toast
import app.melon.base.ui.list.vertSpaceConventional
import app.melon.data.entities.User
import app.melon.user.R
import app.melon.user.ui.widget.editBackgroundAndAvatar
import app.melon.user.ui.widget.editBasicInfo
import app.melon.user.ui.widget.editPersonInfo
import app.melon.user.ui.widget.editUserInterest
import app.melon.user.ui.widget.uploadImageNinePhotoView
import app.melon.util.AppHelper
import app.melon.util.extensions.dpInt
import com.airbnb.epoxy.TypedEpoxyController

class EditProfileController(
    private val action: Action? = null
) : TypedEpoxyController<User>() {

    override fun buildModels(data: User?) {
        editBackgroundAndAvatar {
            id("edit_background_and_avatar")
            avatarUrl(data!!.avatarUrl)
            backgroundUrl(data.backgroundUrl)
            editAvatarListener { action?.onClickEditAvatar() }
            editBackgroundListener { action?.onClickEditBackground() }
        }
        vertSpaceConventional {
            id("edit_basic_info_top_space")
        }
        editBasicInfo {
            id("edit_basic_info")
            item(data!!)
            afterHometownChanged { action?.afterHometownChanged(it) }
            afterCollegeChanged { action?.afterCollegeChanged(it) }
            afterDegreeChanged { action?.afterDegreeChanged(it) }
            afterMajorChanged { action?.afterMajorChanged(it) }
        }
        vertSpaceConventional {
            id("edit_person_info_top_space")
        }
        editPersonInfo {
            id("edit_person_info")
            item(data!!)
            afterBioChanged { action?.afterBioChanged(it) }
        }
        vertSpaceConventional {
            id("edit_user_interest_top_space")
        }
        editUserInterest {
            id("edit_user_interest")
            item(data!!)
            enterInterestPageListener { }
        }
        vertSpaceConventional {
            id("edit_photos_top_space")
        }
        uploadImageNinePhotoView {
            id("upload_image_photo_view")
            buttonRadius(28.dpInt)
            onButtonClickListener { urls, i ->
                val url = urls.getOrNull(i)
                if (url == null) {
                    action?.onClickUploadImage()
                } else {
                    action?.onClickDeleteImage(url, i)
                }
                Toast.makeText(AppHelper.applicationContext, url.toString(), Toast.LENGTH_SHORT).show()
            }
            itemPadding(16.dpInt)
            paddingHorizontal(12.dpInt)
            cornerRadius(24f)
            urls(data?.photos ?: emptyList())
            whRatio(0.67f)
            placeholder(R.drawable.bg_upload_photo_placeholder)
        }
    }

    interface Action {
        fun onClickEditAvatar()
        fun onClickEditBackground()
        fun afterHometownChanged(content: String)
        fun afterCollegeChanged(content: String)
        fun afterMajorChanged(content: String)
        fun afterDegreeChanged(content: String)
        fun afterBioChanged(content: String)
        fun onClickDeleteImage(url: String, position: Int)
        fun onClickUploadImage()
    }
}