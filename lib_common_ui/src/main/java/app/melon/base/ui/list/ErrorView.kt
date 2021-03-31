package app.melon.base.ui.list

import android.widget.TextView
import androidx.annotation.StringRes
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.R
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class ErrorView : EpoxyModelWithHolder<ErrorView.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.view_refresh_error

    @EpoxyAttribute @StringRes var titleRes: Int = R.string.app_common_sorry
    @EpoxyAttribute @StringRes var subtitleRes: Int = R.string.app_common_error_hint
    @EpoxyAttribute var retryListener: () -> Unit = {}

    override fun bind(holder: Holder) {
        with(holder) {
            titleView.setText(titleRes)
            subtitleView.setText(subtitleRes)
            retryView.setOnClickListener { retryListener.invoke() }
        }
    }

    class Holder : BaseEpoxyHolder() {
        internal val titleView: TextView by bind(R.id.error_title)
        internal val subtitleView: TextView by bind(R.id.error_subtitle)
        internal val retryView: TextView by bind(R.id.error_retry)
    }
}