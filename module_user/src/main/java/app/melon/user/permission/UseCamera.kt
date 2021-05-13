package app.melon.user.permission

import android.Manifest
import android.os.Build
import app.melon.permission.PermissionRequest
import app.melon.permission.R

/**
 * We don't need to request camera permission since we are using system camera.
 *
 * But JMessage SDK declare Camera permission in their manifest, so we need to
 * request camera permission to MAKE THEM HAPPY.
 *
 * Will remove JMessage SDK from project in future version.
 */
internal object UseCamera : PermissionRequest(
    permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        arrayOf(Manifest.permission.CAMERA)
    } else {
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    },
    title = R.string.request_camera_title,
    subTitle = R.string.request_camera_subtitle,
    name = "TODO"
)