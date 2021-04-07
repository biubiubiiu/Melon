package app.melon.permission.helper

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import app.melon.permission.PermissionRequestActivity
import app.melon.permission.ReadStorage
import app.melon.util.AppHelper
import app.melon.util.extensions.showToast
import app.melon.util.graphics.GraphicsUtil
import coil.imageLoader
import coil.request.ImageRequest
import java.text.SimpleDateFormat
import java.util.*


// TODO put it with [GalleryActivity]
class SaveHelper(
    private val activity: AppCompatActivity
) {
    private var pendingProcessUrl: String = ""

    fun saveImage(url: String) {
        pendingProcessUrl = url
        checkPermissionAndLoadBitmap(url)
    }

//    private val showWriteStorageRational = with(activity) {
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            when (it.resultCode) {
//                Activity.RESULT_OK -> {
//                    this@SaveHelper.requestWriteStoragePermission.launch(PermissionList.permissionsForSave)
//                }
//                Activity.RESULT_CANCELED -> {
//                    // No-op
//                }
//            }
//        }
//    }
//
//    private val requestWriteStoragePermission = with(activity) {
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->
//            val isAllGranted = !result.values.contains(element = false)
//            if (isAllGranted) {
//                this@SaveHelper.checkPermissionAndLoadBitmap(pendingProcessUrl)
//            } else {
//                val intent = PermissionRequestActivity.prepareIntent(
//                    this,
//                    PermissionDenial(result.filterValues { !it }.keys.toList())
//                )
//                startActivity(intent)
//            }
//        }
//    }

    private fun checkPermissionAndLoadBitmap(bitmapUrl: String): Unit = with(activity) {
        if (bitmapUrl.isEmpty()) return@with
//        if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        if (true) {
            getBitmapFromUrl(bitmapUrl)
        } else {
            val intent = PermissionRequestActivity.prepareIntent(this, ReadStorage)
            showToast("no permission")
//            this@SaveHelper.showWriteStorageRational.launch(intent)
        }
    }

    private fun getBitmapFromUrl(url: String) {
        val request = ImageRequest.Builder(activity)
            .data(url)
            .target(
                onSuccess = { result: Drawable ->
                    val bitmap = GraphicsUtil.drawable2Bitmap(result)
                    saveMediaToStorage(bitmap)
                }
            )
            .build()
        activity.imageLoader.enqueue(request)
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Date())
        val filename = "Melon_${currentDateTime}"
        val resolver = AppHelper.applicationContext.contentResolver
        val imageCollection = MediaStore.Audio.Media.getContentUri(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            } else {
                MediaStore.VOLUME_EXTERNAL
            }
        )
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        resolver.insert(imageCollection, values)
    }
}