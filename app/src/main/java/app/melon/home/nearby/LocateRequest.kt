package app.melon.home.nearby

import app.melon.R
import app.melon.location.LocationHelper
import app.melon.permission.PermissionRequest

object LocateRequest : PermissionRequest(
    permissions = LocationHelper.requiredPermissions,
    title = R.string.request_locate_title,
    subTitle = R.string.request_locate_subtitle,
    name = "Locate"
)