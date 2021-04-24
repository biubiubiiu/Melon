package app.melon.gallery.permission

import android.Manifest
import android.os.Build
import app.melon.gallery.R
import app.melon.permission.PermissionRequest


object SaveImage : PermissionRequest(
    permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        emptyArray()
    } else {
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    },
    title = R.string.save_iamge_permission_rationale_title,
    subTitle = -1,
    name = "Save Image"
)