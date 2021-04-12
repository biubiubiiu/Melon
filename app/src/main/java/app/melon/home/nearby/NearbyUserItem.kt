package app.melon.home.nearby

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.melon.R
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.TagView
import app.melon.data.entities.User
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class NearbyUserItem : EpoxyModelWithHolder<NearbyUserItem.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.item_nearby_user

    @EpoxyAttribute lateinit var user: User
    @EpoxyAttribute lateinit var holderClickListener: () -> Unit

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListeners(holder)
    }

    private fun setupContent(holder: Holder) {
        with(holder) {
            avatarView.load(user.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            usernameView.text = user.username
            userTagView.bind(user, style = TagView.TagStyle.Info())
            userDescriptionView.text = user.description
            schoolView.text = user.school.orEmpty()
            distance.text = "1km" // TODO import location module
        }
    }

    private fun setupListeners(holder: Holder) {
        holder.containerView.setOnClickListener { holderClickListener.invoke() }
    }

    class Holder : BaseEpoxyHolder() {
        val containerView by bind<ViewGroup>(R.id.container_view)
        val avatarView by bind<ImageView>(R.id.nearby_user_avatar)
        val usernameView by bind<TextView>(R.id.nearby_username)
        val userTagView by bind<TagView>(R.id.nearby_user_tag)
        val userDescriptionView by bind<TextView>(R.id.nearby_user_description)
        val schoolView by bind<TextView>(R.id.nearby_user_school)
        val distance by bind<TextView>(R.id.nearby_user_distance)
    }
}