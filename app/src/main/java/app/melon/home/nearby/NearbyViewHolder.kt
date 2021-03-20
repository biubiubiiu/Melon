package app.melon.home.nearby

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.melon.R
import app.melon.data.entities.Feed

class NearbyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val content: TextView = view.findViewById(R.id.content)

    fun bind(item: Feed?) {
        if (item == null) {
            content.text = "placeholder"
        } else {
            content.text = item.content
        }
    }

    companion object {
        fun create(parent: ViewGroup): NearbyViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.nearby_view_item, parent, false)
            return NearbyViewHolder(view)
        }
    }
}