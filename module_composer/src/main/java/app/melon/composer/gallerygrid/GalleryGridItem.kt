package app.melon.composer.gallerygrid

import androidx.recyclerview.widget.DiffUtil
import app.melon.composer.common.MediaStoreImage

internal data class GalleryGridItem(
    val image: MediaStoreImage,
    val isSelected: Boolean,
    val reachMaxSelection: Boolean
) {
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<GalleryGridItem>() {
            override fun areItemsTheSame(oldItem: GalleryGridItem, newItem: GalleryGridItem) =
                oldItem.image.id == newItem.image.id

            override fun areContentsTheSame(oldItem: GalleryGridItem, newItem: GalleryGridItem) =
                oldItem == newItem
        }
    }
}