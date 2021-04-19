package app.melon.user.ui.edit

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import app.melon.permission.PermissionHelper
import app.melon.user.permission.ReadStorage
import app.melon.user.permission.UseCamera
import app.melon.util.AppHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EditHelper(
    private val activity: AppCompatActivity,
    private val onReceiveTakePictureResult: ((Boolean) -> Unit)? = null,
    private val onReceiveUriFromAlbum: ((Uri?) -> Unit)? = null
) : EditOptionsDialogFragment.Listener {

    private val readStoragePermissionHelper = PermissionHelper(activity, ReadStorage)

    private val useCameraPermissionHelper = PermissionHelper(activity, UseCamera)

    private var pendingProcessCameraImageUri: Uri? = null
    private val takeImageFromCamera = with(activity) {
        registerForActivityResult(ActivityResultContracts.TakePicture()) { result: Boolean ->
            onReceiveTakePictureResult?.invoke(result)
            if (!result) {
                val resolver = applicationContext.contentResolver
                val url = pendingProcessCameraImageUri ?: return@registerForActivityResult
                resolver.delete(url, null)
            }
        }
    }

    private val selectImageFromAlbum = with(activity) {
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            onReceiveUriFromAlbum?.invoke(uri)
        }
    }

    fun showEditOptions() {
        val fragment = EditOptionsDialogFragment().also { it.setListener(this) }
        fragment.show(activity.supportFragmentManager, "options")
    }

    override fun onSelectOptionCamera() {
        openCamera()
    }

    override fun onSelectOptionAlbum() {
        openAlbum()
    }

    private fun openCamera() {
        useCameraPermissionHelper.checkPermissions(
            onPermissionAllGranted = {
                val currentDateTime = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(Date())
                val filename = "Melon_${currentDateTime}.jpg"
                val resolver = AppHelper.applicationContext.contentResolver
                val imageCollection = MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                )
                val newImageDetails = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                }
                pendingProcessCameraImageUri = resolver.insert(imageCollection, newImageDetails)
                takeImageFromCamera.launch(pendingProcessCameraImageUri)
            }
        )
    }

    private fun openAlbum() {
        readStoragePermissionHelper.checkPermissions(
            onPermissionAllGranted = {
                selectImageFromAlbum.launch("image/*")
            }
        )
    }
}