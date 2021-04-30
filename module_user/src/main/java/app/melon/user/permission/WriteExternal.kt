package app.melon.user.permission

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Build
import app.melon.permission.PermissionRequest
import app.melon.user.R


/**
 * Check if the app can writes on the shared storage
 *
 * On Android 10 (API 29), we can add media to MediaStore without having to request the
 * [WRITE_EXTERNAL_STORAGE] permission, so we only check on pre-API 29 devices
 */
internal object WriteExternal : PermissionRequest(
    permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        emptyArray()
    } else {
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    },
    title = R.string.request_write_external_message_title,
    subTitle = R.string.request_write_external_message_subtitle,
    name = "WriteExternal"
)