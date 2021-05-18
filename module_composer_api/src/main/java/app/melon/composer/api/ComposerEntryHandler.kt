package app.melon.composer.api

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class DefaultComposerEntryHandler(
    private val activity: ComponentActivity
) : ComposerEntry by ComposerEntryHandleImpl(
    activity.activityResultRegistry,
    activity
)

private class ComposerEntryHandleImpl(
    private val registry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner
) : DefaultLifecycleObserver, ComposerEntry {

    private lateinit var launchComposer: ActivityResultLauncher<ComposerOption>
    private var callback: (ComposerResult?) -> Unit = {}

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        launchComposer = registry.register(
            "launch_composer",
            owner,
            ComposerContract(),
            ActivityResultCallback {
                callback(it)
            }
        )
    }

    override fun launchComposer(option: ComposerOption, callback: (ComposerResult?) -> Unit) {
        this.callback = callback
        launchComposer.launch(option)
    }
}