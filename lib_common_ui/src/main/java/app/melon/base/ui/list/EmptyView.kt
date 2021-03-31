package app.melon.base.ui.list

import android.widget.TextView
import androidx.annotation.StringRes
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class EmptyView : EpoxyModelWithHolder<EmptyView.Holder>() {

    override fun getDefaultLayout() = R.layout.view_empty_list

    @EpoxyAttribute @StringRes var titleRes: Int = R.string.empty_list_title
    @EpoxyAttribute @StringRes var subtitleRes: Int = R.string.empty_list_subtitile

    override fun bind(holder: Holder) {
        with(holder) {
            titleView.setText(titleRes)
            subtitleView.setText(subtitleRes)
        }
    }

    class Holder : BaseEpoxyHolder() {
        internal val titleView by bind<TextView>(R.id.empty_view_title)
        internal val subtitleView by bind<TextView>(R.id.empty_view_subtitle)
    }
}