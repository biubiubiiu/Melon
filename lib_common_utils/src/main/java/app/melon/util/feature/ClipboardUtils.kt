package app.melon.util.feature

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import app.melon.util.AppHelper

object ClipboardUtils {

    fun copyToClipboard(text: String?) {
        val systemService: ClipboardManager =
            AppHelper.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        systemService.setPrimaryClip(ClipData.newPlainText("text", text))
    }
}