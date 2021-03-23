package app.melon.base.ui.list

import android.widget.TextView
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class EmptyView : EpoxyModelWithHolder<EmptyView.Holder>() {

    override fun getDefaultLayout() = R.layout.view_empty_list

    @EpoxyAttribute lateinit var title: String
    @EpoxyAttribute lateinit var subtitle: String

    override fun bind(holder: Holder) {
        with(holder) {
            titleView.text = title
            subtitleView.text = subtitle
        }
    }

    class Holder : BaseEpoxyHolder() {
        val titleView by bind<TextView>(R.id.empty_view_title)
        val subtitleView by bind<TextView>(R.id.empty_view_subtitle)
    }
}