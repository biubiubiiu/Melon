package app.melon.base.ui.gallery.photos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.melon.base.ui.R
import app.melon.base.ui.extensions.resetScale
import coil.load
import com.github.chrisbanes.photoview.PhotoView


class GalleryItemView(layoutInflater: LayoutInflater, container: ViewGroup?) {

    val view: View = layoutInflater.inflate(R.layout.item_gallery, container, false)

    private val photoView: PhotoView = view.findViewById(R.id.image)

     val isScaled: Boolean
        get() = photoView.scale > 1f

    internal fun resetScale() = photoView.resetScale(animate = true)

    fun bind(url: String) {
        photoView.load(url)
    }
}