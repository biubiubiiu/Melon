package app.melon.permission

interface PermissionHelperOwner {
    fun checkPermission(
        request: PermissionRequest,
        onPermissionsGranted: () -> Unit
    )
}