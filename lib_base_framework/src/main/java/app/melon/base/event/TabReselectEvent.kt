package app.melon.base.event

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class TabReselectEvent(
    private val context: Context,
    block: (intent: Intent) -> Unit
) : EventBroadcastManager(block) {

    companion object {
        const val POST_CREATION_EVENT = "event.tab.reselect"
        const val PAGE_NAME = "data.page.name"

        fun sendEvent(context: Context, pageName: String?) {
            val data = Intent(POST_CREATION_EVENT).also {
                it.putExtra(PAGE_NAME, pageName)
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(data)
        }
    }

    init {
        LocalBroadcastManager.getInstance(context)
            .registerReceiver(this, IntentFilter(POST_CREATION_EVENT))
    }

    fun dispose() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
    }
}