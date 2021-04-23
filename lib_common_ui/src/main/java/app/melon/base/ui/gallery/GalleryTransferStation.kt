package app.melon.base.ui.gallery

import android.widget.ImageView
import java.lang.ref.WeakReference


/**
 * Just a temporary solution. Should never exists.
 *
 * TODO: use SharedElement Transition normally
 */
internal object GalleryTransferStation {
    var viewRefs: List<WeakReference<ImageView>> = emptyList()
}