package app.melon.base.ui.list

import androidx.core.widget.ContentLoadingProgressBar
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.R
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class LoadMoreView : EpoxyModelWithHolder<LoadMoreView.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.view_list_load_more

    class Holder : BaseEpoxyHolder() {
        internal val progressbar by bind<ContentLoadingProgressBar>(R.id.content_loading_progressbar)
    }
}