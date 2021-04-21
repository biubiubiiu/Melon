package app.melon.base.event

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


abstract class EventBroadcastManager(private val block: (intent: Intent) -> Unit) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            block(it)
        }
    }
}