package app.melon.composer.selectlocation

import androidx.recyclerview.widget.DiffUtil
import app.melon.data.entities.PoiInfo

internal data class LocationWithSelection(
    val info: PoiInfo,
    val isSelected: Boolean
) {
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<LocationWithSelection>() {
            override fun areItemsTheSame(oldItem: LocationWithSelection, newItem: LocationWithSelection) =
                oldItem.info.poiId == newItem.info.poiId

            override fun areContentsTheSame(oldItem: LocationWithSelection, newItem: LocationWithSelection) =
                oldItem == newItem
        }
    }
}