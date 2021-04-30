package app.melon.user.image

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


internal interface TakePicture {
    fun takePicture(uri: Uri, callback: (Boolean) -> Unit)
}

internal class TakePictureHandler(
    private val activity: ComponentActivity
) : TakePicture by TakePictureHandlerImpl(
    activity.activityResultRegistry,
    activity
)

internal class TakePictureHandlerImpl(
    private val registry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner
) : DefaultLifecycleObserver, TakePicture {

    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private var callback: (Boolean) -> Unit = {}

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        takePicture = registry.register(
            "take_picture",
            owner,
            ActivityResultContracts.TakePicture(),
            ActivityResultCallback<Boolean> {
                callback(it)
            }
        )
    }

    override fun takePicture(uri: Uri, callback: (Boolean) -> Unit) {
        this.callback = callback
        takePicture.launch(uri)
    }
}