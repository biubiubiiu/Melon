package app.melon.user.ui.widget

import android.widget.TextView
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.data.entities.User
import app.melon.user.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass
abstract class EditUserInterest : EpoxyModelWithHolder<EditUserInterest.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.view_edit_user_interest

    @EpoxyAttribute lateinit var item: User
    @EpoxyAttribute lateinit var enterInterestPageListener: () -> Unit

    override fun bind(holder: Holder) {
        with(holder) {
            interestView.text = "TODO"
            interestView.setOnClickListener { enterInterestPageListener.invoke() }
        }
    }

    class Holder : BaseEpoxyHolder() {
        internal val interestView: TextView by bind(R.id.user_interest)
    }
}