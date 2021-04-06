package app.melon.user.edit

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import app.melon.permission.PermissionDenial
import app.melon.permission.PermissionList
import app.melon.permission.PermissionRequestActivity
import app.melon.permission.ReadStorage
import app.melon.permission.UseCamera
import app.melon.user.ui.image.EditOptionsDialogFragment
import app.melon.util.AppHelper
import app.melon.util.extensions.hasPermission
import app.melon.util.extensions.hasPermissions
import java.text.SimpleDateFormat
import java.util.*

class EditHelper(
    private val activity: AppCompatActivity,
    private val onReceiveTakePictureResult: ((Boolean) -> Unit)? = null,
    private val onReceiveUriFromAlbum: ((Uri?) -> Unit)? = null
) : EditOptionsDialogFragment.Listener {

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

    private val showReadStorageRational = with(activity) {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    requestReadStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                Activity.RESULT_CANCELED -> {
                    // No-op
                }
            }
        }
    }

    private val showUseCameraRational = with(activity) {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    this@EditHelper.requestCameraPermission.launch(PermissionList.permissionsForCamera)
                }
                Activity.RESULT_CANCELED -> {
                    // No-op
                }
            }
        }
    }

    private val requestReadStoragePermission = with(activity) {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                this@EditHelper.openAlbum()
            } else {
                val intent = PermissionRequestActivity.prepareIntent(
                    this,
                    PermissionDenial(listOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                )
                startActivity(intent)
            }
        }
    }

    private val requestCameraPermission = with(activity) {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->
            val isAllGranted = !result.values.contains(element = false)
            if (isAllGranted) {
                this@EditHelper.openCamera()
            } else {
                val intent = PermissionRequestActivity.prepareIntent(
                    this,
                    PermissionDenial(result.filterValues { !it }.keys.toList())
                )
                startActivity(intent)
            }
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

    private fun openCamera(): Unit = with(activity) {
        if (activity.hasPermissions(*PermissionList.permissionsForCamera)) {
            val currentDateTime = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Date())
            val filename = "Melon_${currentDateTime}.jpg"
            val resolver = AppHelper.applicationContext.contentResolver
            val imageCollection = MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )
            val newSongDetails = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            }
            pendingProcessCameraImageUri = resolver.insert(imageCollection, newSongDetails)
            takeImageFromCamera.launch(pendingProcessCameraImageUri)
        } else {
            val intent = PermissionRequestActivity.prepareIntent(this, UseCamera)
            this@EditHelper.showUseCameraRational.launch(intent)
        }
    }

    private fun openAlbum(): Unit = with(activity) {
        if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            selectImageFromAlbum.launch("image/*")
        } else {
            val intent = PermissionRequestActivity.prepareIntent(this, ReadStorage)
            this@EditHelper.showReadStorageRational.launch(intent)
        }
    }
}