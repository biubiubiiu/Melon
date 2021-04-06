package app.melon.user.ui.widget

import android.widget.ImageView
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.transform.OverlayTransformation
import app.melon.user.R
import app.melon.util.extensions.getResourceDrawable
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass
abstract class EditBackgroundAndAvatar : EpoxyModelWithHolder<EditBackgroundAndAvatar.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.view_change_background_avatar

    @EpoxyAttribute lateinit var avatarUrl: String
    @EpoxyAttribute lateinit var backgroundUrl: String

    @EpoxyAttribute lateinit var editAvatarListener: () -> Unit
    @EpoxyAttribute lateinit var editBackgroundListener: () -> Unit

    override fun bind(holder: Holder) {
        with(holder) {
            val overlay = getResourceDrawable(R.drawable.edit_image_overlay)!!
            background.load(backgroundUrl) {
                transformations(
                    OverlayTransformation(overlay, resize = true)
                )
            }
            avatar.load(avatarUrl) {
                transformations(
                    OverlayTransformation(overlay, resize = true),
                    CircleCropTransformation()
                )
            }

            background.setOnClickListener { editBackgroundListener.invoke() }
            avatar.setOnClickListener { editAvatarListener.invoke() }
        }
    }

    class Holder : BaseEpoxyHolder() {
        internal val background: ImageView by bind(R.id.background)
        internal val avatar: ImageView by bind(R.id.avatar)
    }
}