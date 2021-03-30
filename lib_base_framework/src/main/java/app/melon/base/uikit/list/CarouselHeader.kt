package app.melon.base.uikit.list

import android.widget.TextView
import app.melon.base.R
import app.melon.base.ui.BaseEpoxyHolder
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class CarouselHeader : EpoxyModelWithHolder<CarouselHeader.Holder>() {

    @EpoxyAttribute lateinit var clickListener: () -> Unit

    @EpoxyAttribute lateinit var title: String
    @EpoxyAttribute lateinit var trailingText: String

    override fun getDefaultLayout() = R.layout.view_carousel_header

    override fun bind(holder: Holder) {
        with(holder) {
            titleView.text = title
            trailingView.text = trailingText
            trailingView.setOnClickListener { clickListener }
        }
    }

    class Holder : BaseEpoxyHolder() {
        val titleView by bind<TextView>(R.id.header_title)
        val trailingView by bind<TextView>(R.id.header_trailing)
    }
}