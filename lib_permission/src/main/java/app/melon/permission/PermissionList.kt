package app.melon.permission

import android.Manifest
import android.os.Build

object PermissionList {

    val permissionsForCamera = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        arrayOf(Manifest.permission.CAMERA)
    } else {
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}