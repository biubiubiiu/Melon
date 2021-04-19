package app.melon.user.permission

import android.Manifest
import app.melon.permission.PermissionRequest
import app.melon.permission.R

object ReadStorage : PermissionRequest(
    permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
    title = R.string.request_read_stroage_title,
    subTitle = -1,
    name = "TODO"
)