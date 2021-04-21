package app.melon.base.event

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


/**
 * Taken from: https://kalpeshchandora12.medium.com/event-handling-with-localbroadcastmanager-android-b62ae5759b5e
 */
abstract class EventBroadcastManager(private val block: (intent: Intent) -> Unit) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            block(it)
        }
    }
}