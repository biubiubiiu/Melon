package app.melon.permission

import java.io.Serializable

sealed class PermissionActions : Serializable {
    abstract val permissions: Array<out String>
    abstract val title: Int
    abstract val subTitle: Int
    abstract val name: String
}

open class PermissionRequest(
    override val permissions: Array<out String>,
    override val title: Int,
    override val subTitle: Int,
    override val name: String
) : PermissionActions()

class PermissionDenial(
    override val permissions: Array<out String>
) : PermissionActions() {
    override val title: Int = R.string.request_fail_title
    override val subTitle: Int = R.string.request_fail_subtitle
    override val name: String = javaClass.name
}