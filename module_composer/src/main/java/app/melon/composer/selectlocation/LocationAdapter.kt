package app.melon.composer.selectlocation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.melon.composer.R
import app.melon.location.LocationHelper
import app.melon.util.formatter.MelonDistanceFormatter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


internal class LocationAdapter @AssistedInject constructor(
    @Assisted private val onSelect: (LocationWithSelection) -> Unit,
    @Assisted private val onClear: () -> Unit,
    private val locationHelper: LocationHelper,
    private val formatter: MelonDistanceFormatter
) : ListAdapter<LocationWithSelection, LocationViewHolder>(
    LocationWithSelection.DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder.create(parent, onSelect, onClear)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val item = getItem(position)
        holder.rootView.tag = item
        holder.isSelected = item.isSelected
        holder.titleView.text = item.info.poiName
        holder.distanceView.text = formatter.formatDistance(
            locationHelper.getMyDistanceTo(item.info.longitude, item.info.latitude)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_location
    }

    @AssistedFactory
    interface Factory {
        fun create(
            onSelect: (LocationWithSelection) -> Unit,
            onClear: () -> Unit
        ): LocationAdapter
    }
}

internal class LocationViewHolder(
    view: View,
    onSelect: (LocationWithSelection) -> Unit,
    onClear: () -> Unit
) : RecyclerView.ViewHolder(view) {

    internal val rootView = view
    internal val clearView: View = view.findViewById(R.id.clear)
    internal val titleView: TextView = view.findViewById(R.id.name)
    internal val distanceView: TextView = view.findViewById(R.id.distance)

    var isSelected: Boolean = false
        set(value) {
            field = value
            rootView.isSelected = value
            clearView.isVisible = isSelected
        }

    init {
        clearView.setOnClickListener {
            onClear.invoke()
        }
        rootView.setOnClickListener {
            val item = rootView.tag as? LocationWithSelection ?: return@setOnClickListener
            onSelect.invoke(item)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onSelect: (LocationWithSelection) -> Unit,
            onClear: () -> Unit
        ): LocationViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
            return LocationViewHolder(view, onSelect, onClear)
        }
    }
}