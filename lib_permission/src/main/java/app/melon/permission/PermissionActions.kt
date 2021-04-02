package app.melon.permission

import androidx.annotation.StringRes
import java.io.Serializable

sealed class PermissionActions : Serializable {
    abstract val title: Int
    abstract val subTitle: Int
}

abstract class PermissionRequest : PermissionActions()
class PermissionDenial(
    val permissions: List<String>
) : PermissionActions() {
    override val title: Int = R.string.request_fail_title
    override val subTitle: Int = R.string.request_fail_subtitle
}

object ReadStorage : PermissionRequest() {
    @StringRes override val title: Int = R.string.request_read_stroage_title
    @StringRes override val subTitle: Int = -1
}

object UseCamera : PermissionRequest() {
    @StringRes override val title: Int = R.string.request_camera_title
    @StringRes override val subTitle: Int = R.string.request_camera_subtitle
}
