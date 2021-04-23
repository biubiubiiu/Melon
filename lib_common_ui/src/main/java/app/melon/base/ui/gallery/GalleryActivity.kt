package app.melon.base.ui.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import app.melon.base.ui.R
import app.melon.base.ui.gallery.viewer.ImageViewerView
import java.lang.ref.WeakReference


class GalleryActivity : AppCompatActivity(R.layout.activity_gallery) {

    private lateinit var viewerView: ImageViewerView

    private val urls: Array<String> get() = intent.getStringArrayExtra(KEY_URL_LIST)!!
    private val startPosition: Int get() = intent.getIntExtra(KEY_START_POSITION, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewerView()
    }

    override fun onBackPressed() {
        viewerView.close()
    }

    private fun setupViewerView() {
        viewerView = findViewById(R.id.image_viewer)
        viewerView.apply {
            overlayView = null
            setImages(urls.asList(), startPosition)

            onPageChange = { position ->
                updateTransitionImage(getExternalViewAt(position))
            }
            onDismiss = { this@GalleryActivity.finish() }
        }
        viewerView.open(getExternalViewAt(startPosition), true)
    }

    private fun getExternalViewAt(index: Int): ImageView? {
        return GalleryTransferStation.viewRefs.getOrNull(index)?.get()
    }

    companion object {
        private const val KEY_URL_LIST = "KEY_URL_LIST"
        private const val KEY_START_POSITION = "KEY_START_POSITION"

        fun start(
            context: Context,
            urls: List<String>,
            startPosition: Int = 0,
            viewRefs: List<ImageView> = emptyList()
        ) {
            val intent = Intent(context, GalleryActivity::class.java).apply {
                putExtra(KEY_URL_LIST, urls.toTypedArray())
                putExtra(KEY_START_POSITION, startPosition)
            }
            GalleryTransferStation.viewRefs = viewRefs.map { WeakReference(it) }
            context.startActivity(intent)
        }
    }
}