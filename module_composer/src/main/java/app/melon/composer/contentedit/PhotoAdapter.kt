package app.melon.composer.contentedit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.melon.composer.BuildConfig
import app.melon.composer.R
import app.melon.composer.common.MediaStoreImage
import coil.load


internal class PhotoAdapter(
    private val onRemoveClick: (MediaStoreImage) -> Unit
) : ListAdapter<MediaStoreImage, PhotoViewHolder>(MediaStoreImage.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view, onRemoveClick)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val image = getItem(position)
        holder.rootView.tag = image
        holder.imageView.load(image.contentUri) {
            allowHardware(!BuildConfig.DEBUG)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_photo
    }
}

internal class PhotoViewHolder(
    view: View,
    onRemoveClick: (MediaStoreImage) -> Unit
) : RecyclerView.ViewHolder(view) {

    internal val rootView = view
    internal val imageView: ImageView = view.findViewById(R.id.photo)
    internal val deleteView: View = view.findViewById(R.id.remove)

    init {
        deleteView.setOnClickListener {
            val image = rootView.tag as? MediaStoreImage ?: return@setOnClickListener
            onRemoveClick.invoke(image)
        }
    }
}