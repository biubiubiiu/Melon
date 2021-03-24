package app.melon.base.ui.list

import android.view.View
import androidx.annotation.DrawableRes
import app.melon.base.ui.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class VertSpaceMicro : EpoxyModel<View>() {
    override fun getDefaultLayout() = R.layout.view_vert_spacer_micro

    @EpoxyAttribute
    @DrawableRes var bgColor: Int = R.color.bgPrimary

    override fun bind(view: View) {
        view.setBackgroundResource(bgColor)
    }
}

@EpoxyModelClass
abstract class VertSpaceSmall : EpoxyModel<View>() {
    override fun getDefaultLayout() = R.layout.view_vert_spacer_small

    @EpoxyAttribute
    @DrawableRes var bgColor: Int = R.color.bgPrimary

    override fun bind(view: View) {
        view.setBackgroundResource(bgColor)
    }
}

@EpoxyModelClass
abstract class VertSpaceNormal : EpoxyModel<View>() {
    override fun getDefaultLayout() = R.layout.view_vert_spacer_normal

    @EpoxyAttribute
    @DrawableRes var bgColor: Int = R.color.bgPrimary

    override fun bind(view: View) {
        view.setBackgroundResource(bgColor)
    }
}

@EpoxyModelClass
abstract class VertSpaceConventional : EpoxyModel<View>() {
    override fun getDefaultLayout() = R.layout.view_vert_spacer_conventional

    @EpoxyAttribute
    @DrawableRes var bgColor: Int = R.color.bgPrimary

    override fun bind(view: View) {
        view.setBackgroundResource(bgColor)
    }
}