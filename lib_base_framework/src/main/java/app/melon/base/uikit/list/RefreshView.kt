package app.melon.base.uikit.list

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import app.melon.base.R
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class RefreshView : EpoxyModel<View>() {
    override fun getDefaultLayout(): Int = R.layout.view_refreshing
}