package app.melon.composer.gallerygrid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.melon.composer.R
import app.melon.composer.common.MediaStoreImage
import app.melon.composer.ui.CheckView
import coil.load


internal class GalleryItemAdapter(
    private val onClick: (MediaStoreImage, Boolean) -> Unit
) : ListAdapter<GalleryGridItem, ImageViewHolder>(GalleryGridItem.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_gallery_grid, parent, false)
        return ImageViewHolder(view) { item ->
            onClick(item.image, item.isSelected)
        }
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        val mediaStoreImage = item.image
        holder.rootView.tag = item
        holder.imageView.load(mediaStoreImage.contentUri)
        holder.checkView.setChecked(item.isSelected)
        holder.enableCheck = !item.reachMaxSelection
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_gallery_grid
    }
}

/**
 * Basic [RecyclerView.ViewHolder] for our gallery.
 */
internal class ImageViewHolder(
    view: View,
    onClick: (GalleryGridItem) -> Unit
) : RecyclerView.ViewHolder(view) {

    internal val rootView = view
    internal val imageView: ImageView = view.findViewById(R.id.image)
    internal val checkView: CheckView = view.findViewById(R.id.check_view)

    var enableCheck: Boolean = true
        set(value) {
            field = value
            checkView.isEnabled = field
        }

    init {
        checkView.setOnClickListener {
            if (!enableCheck) return@setOnClickListener
            val image = rootView.tag as? GalleryGridItem ?: return@setOnClickListener
            onClick(image)
        }
    }
}