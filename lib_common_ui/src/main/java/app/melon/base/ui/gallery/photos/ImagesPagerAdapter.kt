package app.melon.base.ui.gallery.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


internal class ImagesPagerAdapter(
    private val images: List<String>
) : RecyclerView.Adapter<GalleryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItemViewHolder {
        return GalleryItemViewHolder(GalleryItemView(LayoutInflater.from(parent.context), parent))
    }

    override fun onBindViewHolder(holder: GalleryItemViewHolder, position: Int) = holder.bind(images[position])

    override fun getItemCount() = images.size
}

class GalleryItemViewHolder internal constructor(
    private val view: GalleryItemView
) : RecyclerView.ViewHolder(view.view) {

    internal val isScaled: Boolean
        get() = view.isScaled

    internal fun bind(url: String) {
        view.bind(url)
    }
}