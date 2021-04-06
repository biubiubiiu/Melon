package app.melon.user.ui.widget

import android.widget.EditText
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.data.entities.User
import app.melon.user.R
import app.melon.util.extensions.afterTextChanged
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass
abstract class EditPersonInfo : EpoxyModelWithHolder<EditPersonInfo.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.view_edit_personal_info

    @EpoxyAttribute lateinit var item: User

    @EpoxyAttribute var afterBioChanged: ((String) -> Unit)? = null

    override fun bind(holder: Holder) {
        with(holder) {
            bioInput.setText(item.description)

            bioInput.afterTextChanged { afterBioChanged?.invoke(it) }
        }
    }

    class Holder : BaseEpoxyHolder() {
        internal val bioInput: EditText by bind(R.id.bio_input)
    }
}