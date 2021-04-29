package app.melon.poi.ui.widget

import android.view.View
import android.widget.TextView
import app.melon.base.ui.BaseEpoxyHolder
import app.melon.base.ui.extensions.makeGone
import app.melon.base.ui.extensions.makeVisible
import app.melon.location.SimplifiedLocation
import app.melon.poi.R
import app.melon.poi.data.PoiStruct
import app.melon.util.extensions.getResourceString
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.facebook.shimmer.ShimmerFrameLayout


@EpoxyModelClass
internal abstract class LocationInfo : EpoxyModelWithHolder<LocationInfo.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.view_location_info

    @EpoxyAttribute lateinit var navigationListener: (SimplifiedLocation) -> Unit
    @EpoxyAttribute var item: PoiStruct? = null

    override fun bind(holder: Holder) {
        setupContent(holder)
        setupListener(holder)
    }

    private fun setupContent(holder: Holder) = with(holder) {
        val data = item
        if (data == null) {
            shimmerContainer.makeVisible()
            contentContainer.makeGone()
            shimmerContainer.startShimmer()
        } else {
            shimmerContainer.stopShimmer()
            shimmerContainer.makeGone()
            contentContainer.makeVisible()
            nameView.text = data.name
            districtAndTypeView.text =
                getResourceString(R.string.poi_district_and_type, data.district, data.type)
        }
    }

    private fun setupListener(holder: Holder) = with(holder) {
        navigationButton.setOnClickListener {
            item?.let {
                navigationListener.invoke(it.location)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: Holder) {
        holder.shimmerContainer.stopShimmer()
        super.onViewDetachedFromWindow(holder)
    }

    internal class Holder : BaseEpoxyHolder() {
        internal val shimmerContainer: ShimmerFrameLayout by bind(R.id.shimmer_container)
        internal val contentContainer: View by bind(R.id.content_root)
        internal val nameView: TextView by bind(R.id.name)
        internal val districtAndTypeView: TextView by bind(R.id.info)
        internal val navigationButton: View by bind(R.id.navigate)
    }
}