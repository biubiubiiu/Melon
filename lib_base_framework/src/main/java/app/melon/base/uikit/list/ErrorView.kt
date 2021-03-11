package app.melon.base.uikit.list

import android.view.View
import app.melon.base.R
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class ErrorView : EpoxyModel<View>() {
    override fun getDefaultLayout(): Int = R.layout.view_refresh_error
}