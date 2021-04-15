package app.melon.group

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.data.entities.Group
import coil.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass
abstract class GroupItem : EpoxyModelWithHolder<GroupItem.Holder>() {

    @EpoxyAttribute lateinit var clickListener: (Group) -> Unit

    @EpoxyAttribute lateinit var showPicUrl: String
    @EpoxyAttribute lateinit var groupName: String
    @EpoxyAttribute @DrawableRes var showPicId: Int? = null

    override fun getDefaultLayout(): Int = R.layout.item_interest_group

    override fun bind(holder: Holder) {
        with(holder) {
            if (showPicId != null) {
                showPic.load(showPicId!!)
            } else {
                showPic.load(showPicUrl)
            }
            groupNameView.text = groupName
        }
    }

    class Holder : BaseEpoxyHolder() {
        internal val showPic by bind<ImageView>(R.id.interest_group_pic)
        internal val groupNameView by bind<TextView>(
            R.id.interest_group_name
        )
    }

}