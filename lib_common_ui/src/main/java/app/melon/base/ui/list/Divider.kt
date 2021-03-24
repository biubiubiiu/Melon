package app.melon.base.ui.list

import android.view.View
import app.melon.base.ui.R
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class Divider : EpoxyModel<View>() {
    override fun getDefaultLayout(): Int = R.layout.common_ui_divider
}