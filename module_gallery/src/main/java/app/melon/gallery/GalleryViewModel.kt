package app.melon.gallery

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.melon.util.graphics.GraphicsUtil
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class GalleryViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val context: Context
        get() = getApplication()

    @Suppress("BlockingMethodInNonBlockingContext")
    fun saveImageFromInternet(url: String, callback: () -> Unit) {
        viewModelScope.launch {
            val imageUri = createPhotoUri(url) ?: return@launch
            val request = createImageRequest(url)
            withContext(Dispatchers.IO) {
                val drawable = context.imageLoader.execute(request).drawable ?: return@withContext
                val bitmap = GraphicsUtil.drawable2Bitmap(drawable)
                val bos = ByteArrayOutputStream()
                bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
                ByteArrayInputStream(bos.toByteArray()).use { input ->
                    context.contentResolver.openOutputStream(imageUri, "w")?.use {
                        input.copyTo(it)
                        withContext(Dispatchers.Main) {
                            callback.invoke()
                        }
                    }
                }
            }
        }
    }

    private suspend fun createPhotoUri(url: String): Uri? {
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val extension = if (url.contains(".")) {
            url.substring(url.lastIndexOf("."))
        } else {
            "jpg"
        }
        return withContext(Dispatchers.IO) {
            val newImage = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, generateFilename(extension))
            }

            // This method will perform a binder transaction which is better to execute off the main thread
            context.contentResolver.insert(imageCollection, newImage)
        }
    }

    private fun createImageRequest(url: String) =
        ImageRequest.Builder(context)
            .data(url)
            .build()

    private fun generateFilename(extension: String): String {
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(Date())
        return "Melon_${currentDateTime}.$extension"
    }
}