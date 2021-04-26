package app.melon.composer.permission

import android.Manifest
import app.melon.composer.R
import app.melon.permission.PermissionRequest

internal object AccessGallery : PermissionRequest(
    permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
    title = R.string.request_read_stroage_title,
    subTitle = -1,
    name = "AccessGallery"
)