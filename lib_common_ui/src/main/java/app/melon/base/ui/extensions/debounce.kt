package app.melon.base.ui.extensions

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    delayMillis: Long = 500L,
    scope: CoroutineScope,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (debounceJob == null) {
            debounceJob = scope.launch {
                action(param)
                delay(delayMillis)
                debounceJob = null
            }
        }
    }
}

fun setDebouncedListener(view: View, onClickListener: View.OnClickListener) {
    val clickWithDebounce: (view: View) -> Unit =
        debounce(scope = MainScope()) {
            onClickListener.onClick(it)
        }
    view.setOnClickListener(clickWithDebounce)
}