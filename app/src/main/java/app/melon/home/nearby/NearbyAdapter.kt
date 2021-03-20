package app.melon.home.nearby

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import app.melon.data.entities.Feed
import javax.inject.Inject

class NearbyAdapter @Inject constructor() : PagingDataAdapter<Feed, NearbyViewHolder>(COMPARATOR) {
    override fun onBindViewHolder(holder: NearbyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyViewHolder {
        return NearbyViewHolder.create(parent)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Feed>() {
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean =
                oldItem.feedId == newItem.feedId

            override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean =
                oldItem == newItem
        }
    }
}