package app.melon.base.ui.list

import android.view.View
import app.melon.base.ui.R
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class VertSpaceMicro : EpoxyModel<View>() {
    override fun getDefaultLayout() = R.layout.view_vert_spacer_micro
}

@EpoxyModelClass
abstract class VertSpaceSmall : EpoxyModel<View>() {
    override fun getDefaultLayout() = R.layout.view_vert_spacer_small
}

@EpoxyModelClass
abstract class VertSpaceNormal : EpoxyModel<View>() {
    override fun getDefaultLayout() = R.layout.view_vert_spacer_normal
}