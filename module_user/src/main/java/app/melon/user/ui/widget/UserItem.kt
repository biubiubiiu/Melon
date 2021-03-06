package app.melon.user.ui.widget

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.TagView
import app.melon.data.entities.User
import app.melon.location.LocationHelper
import app.melon.user.R
import app.melon.util.formatter.MelonDistanceFormatter
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass
abstract class UserItem : EpoxyModelWithHolder<UserItem.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.item_user

    @EpoxyAttribute lateinit var user: User
    @EpoxyAttribute lateinit var holderClickListener: (User) -> Unit

    @EpoxyAttribute lateinit var locationHelper: LocationHelper
    @EpoxyAttribute lateinit var formatter: MelonDistanceFormatter

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListeners(holder)
    }

    private fun setupContent(holder: Holder) = with(holder) {
        avatarView.load(user.avatarUrl) {
            transformations(CircleCropTransformation())
        }
        usernameView.text = user.username.orEmpty()
        userTagView.render(style = TagView.TagStyle.Info(user))
        userDescriptionView.text = user.description.orEmpty()
        schoolView.text = user.school.orEmpty()

        val userLocation = user.location
        if (userLocation != null) {
            distance.isVisible = true
            distance.text = formatter.formatDistance(
                distance = locationHelper.getMyDistanceTo(
                    userLocation.longitude, userLocation.latitude
                )
            )
        } else {
            distance.isVisible = false
        }
    }

    private fun setupListeners(holder: Holder) {
        holder.containerView.setOnClickListener { holderClickListener.invoke(user) }
    }

    class Holder : BaseEpoxyHolder() {
        internal val containerView by bind<ViewGroup>(R.id.container_view)
        internal val avatarView by bind<ImageView>(R.id.avatar)
        internal val usernameView by bind<TextView>(R.id.username)
        internal val userTagView by bind<TagView>(R.id.tag)
        internal val userDescriptionView by bind<TextView>(R.id.description)
        internal val schoolView by bind<TextView>(R.id.school)
        internal val distance by bind<TextView>(R.id.distance)
    }
}