package app.melon.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import app.melon.gallery.permission.SaveImage
import app.melon.gallery.viewer.ImageViewerView
import app.melon.permission.PermissionHelper
import app.melon.util.extensions.showToast
import app.melon.util.extensions.weakRef


class GalleryActivity : AppCompatActivity(R.layout.activity_gallery) {

    private lateinit var viewerView: ImageViewerView
    private lateinit var viewerOverlay: ImageViewerOverlay

    private val urls: Array<String> get() = intent.getStringArrayExtra(KEY_URL_LIST)!!
    private val startPosition: Int get() = intent.getIntExtra(KEY_START_POSITION, 0)

    private val saveImagePermissionHelper = PermissionHelper(this, SaveImage)

    private val viewModel: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupOverlayView()
        setupViewerView()
    }

    override fun onBackPressed() {
        viewerView.close()
    }

    private fun setupViewerView() {
        viewerView = findViewById(R.id.image_viewer)
        viewerView.apply {
            overlayView = viewerOverlay
            setImages(urls.asList(), startPosition)

            onPageChange = { position ->
                updateTransitionImage(getExternalViewAt(position))
            }
            onDismiss = { this@GalleryActivity.finish() }
        }
        viewerView.open(transitionImageView = getExternalViewAt(startPosition), animate = true)
    }

    private fun setupOverlayView() {
        viewerOverlay = ImageViewerOverlay(this).apply {
            onBackClick = { this@GalleryActivity.onBackPressed() }
            onSaveClick = {
                val position = viewerView.currentPosition
                saveImage(urls[position])
            }
        }
    }

    private fun getExternalViewAt(index: Int): ImageView? {
        return GalleryTransferStation.viewRefs.getOrNull(index)?.get()
    }

    private fun saveImage(url: String) {
        saveImagePermissionHelper.checkPermissions(
            onPermissionAllGranted = {
                viewModel.saveImageFromInternet(url, callback = {
                    showToast(R.string.image_saved)
                })
            }
        )
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
            GalleryTransferStation.viewRefs = viewRefs.map { it.weakRef() }
            context.startActivity(intent)
        }
    }
}