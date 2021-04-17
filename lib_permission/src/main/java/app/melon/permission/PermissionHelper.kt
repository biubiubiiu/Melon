package app.melon.permission

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import app.melon.util.extensions.hasPermissions


class PermissionHelper(
    private val activity: ComponentActivity,
    private val request: PermissionRequest
) {

    private lateinit var onPermissionRequestCanceled: (() -> Unit)
    private lateinit var onPermissionAllGranted: (() -> Unit)
    private lateinit var onPermissionNotGranted: ((List<String>) -> Unit)

    private val showPermissionRationale = with(activity) {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                Activity.RESULT_OK -> requestLocatingPermission.launch(request.permissions) // proceed
                Activity.RESULT_CANCELED -> onPermissionRequestCanceled.invoke()
            }
        }
    }

    private val requestLocatingPermission = with(activity) {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->
            val isAllGranted = !result.values.contains(element = false)
            if (isAllGranted) {
                onPermissionAllGranted.invoke()
            } else {
                val deniedPermissions = result.filterValues { !it }.keys.toList()
                onPermissionNotGranted.invoke(deniedPermissions)
            }
        }
    }

    fun checkPermissions(
        onPermissionAllGranted: (() -> Unit) = { }, // No-op
        onPermissionRequestCanceled: (() -> Unit) = { }, // No-op
        onPermissionNotGranted: ((List<String>) -> Unit) = {
            activity.startActivity(PermissionRequestActivity.prepareIntent(activity, PermissionDenial(it.toTypedArray())))
        }
    ) {
        this.onPermissionAllGranted = onPermissionAllGranted
        this.onPermissionRequestCanceled = onPermissionRequestCanceled
        this.onPermissionNotGranted = onPermissionNotGranted

        if (!activity.hasPermissions(*request.permissions)) {
            val intent = PermissionRequestActivity.prepareIntent(activity, request)
            showPermissionRationale.launch(intent)
        } else {
            onPermissionAllGranted.invoke()
        }
    }
}