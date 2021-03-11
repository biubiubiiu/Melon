package app.melon.base.uikit.list

import androidx.core.widget.ContentLoadingProgressBar
import app.melon.base.R
import app.melon.base.framework.BaseEpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class LoadMoreView : EpoxyModelWithHolder<LoadMoreView.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.view_list_load_more

    class Holder : BaseEpoxyHolder() {
        val progressbar by bind<ContentLoadingProgressBar>(R.id.content_loading_progressbar)
    }
}