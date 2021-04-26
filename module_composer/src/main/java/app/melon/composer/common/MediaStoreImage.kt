package app.melon.composer.common

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import java.util.*

/**
 * Simple data class to hold information about an image included in the device's MediaStore.
 */
internal data class MediaStoreImage(
    val id: Long,
    val displayName: String,
    val dateAdded: Date,
    val width: Int,
    val height: Int,
    val contentUri: Uri
) {
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<MediaStoreImage>() {
            override fun areItemsTheSame(oldItem: MediaStoreImage, newItem: MediaStoreImage) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MediaStoreImage, newItem: MediaStoreImage) =
                oldItem == newItem
        }
    }
}