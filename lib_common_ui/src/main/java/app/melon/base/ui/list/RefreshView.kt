package app.melon.base.ui.list

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import app.melon.base.ui.R
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class RefreshView : EpoxyModel<View>() {
    override fun getDefaultLayout(): Int = R.layout.view_refreshing
}