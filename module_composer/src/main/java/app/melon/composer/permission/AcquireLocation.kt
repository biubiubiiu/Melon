package app.melon.composer.permission

import app.melon.composer.R
import app.melon.location.LocationHelper
import app.melon.permission.PermissionRequest

internal object AcquireLocation : PermissionRequest(
    permissions = LocationHelper.requiredPermissions,
    title = R.string.request_locate_title,
    subTitle = R.string.request_locate_subtitle,
    name = "AcquireLocation"
)