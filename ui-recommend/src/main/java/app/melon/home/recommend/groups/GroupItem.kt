package app.melon.home.recommend.groups

import android.widget.ImageView
import android.widget.TextView
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.data.entities.InterestGroup
import app.melon.home.recommend.R
import coil.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class GroupItem : EpoxyModelWithHolder<GroupItem.Holder>() {

    @EpoxyAttribute lateinit var clickListener: (InterestGroup) -> Unit

    @EpoxyAttribute lateinit var showPicUrl: String
    @EpoxyAttribute lateinit var groupName: String

    override fun getDefaultLayout(): Int = R.layout.item_interest_group

    override fun bind(holder: Holder) {
        with(holder) {
            showPic.load(showPicUrl)
            groupNameView.text = groupName
        }
    }

    class Holder : BaseEpoxyHolder() {
        val showPic by bind<ImageView>(R.id.interest_group_pic)
        val groupNameView by bind<TextView>(
            R.id.interest_group_name
        )
    }

}