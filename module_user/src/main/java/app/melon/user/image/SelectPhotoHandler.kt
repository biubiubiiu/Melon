package app.melon.user.image

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import app.melon.util.contracts.GetContentWithMimeType

internal interface SelectPhoto {
    fun selectPhoto(input: Pair<String, List<String>>, callback: (Uri?) -> Unit)
}

internal class SelectPhotoHandler(
    private val activity: ComponentActivity
) : SelectPhoto by SelectPhotoHandlerImpl(
    activity.activityResultRegistry,
    activity
)

internal class SelectPhotoHandlerImpl(
    private val registry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner
) : DefaultLifecycleObserver, SelectPhoto {

    private lateinit var selectPhoto: ActivityResultLauncher<Pair<String, List<String>>>
    private var callback: (Uri?) -> Unit = {}

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        selectPhoto = registry.register(
            "select_photo",
            owner,
            GetContentWithMimeType,
            ActivityResultCallback {
                callback(it)
            }
        )
    }

    override fun selectPhoto(input: Pair<String, List<String>>, callback: (Uri?) -> Unit) {
        this.callback = callback
        selectPhoto.launch(input)
    }
}