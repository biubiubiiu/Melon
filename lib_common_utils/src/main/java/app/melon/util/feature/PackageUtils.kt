package app.melon.util.feature

import android.content.pm.PackageManager
import app.melon.util.AppHelper


object PackageUtils {

    fun isPackageInstalled(packageName: String): Boolean {
        val packageManager = AppHelper.applicationContext.packageManager
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}