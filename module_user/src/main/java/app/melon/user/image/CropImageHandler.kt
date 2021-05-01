package app.melon.user.image

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal interface CropImage {
    fun cropImage(intent: Intent, callback: (ActivityResult) -> Unit)
}

internal class CropImageHandler(
    private val activity: ComponentActivity
) : CropImage by CropImageHandlerImpl(
    activity.activityResultRegistry,
    activity
)

internal class CropImageHandlerImpl(
    private val registry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner
) : DefaultLifecycleObserver, CropImage {

    private lateinit var cropImage: ActivityResultLauncher<Intent>
    private var callback: (ActivityResult) -> Unit = {}

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        cropImage = registry.register(
            "crop_image",
            owner,
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                callback(it)
            }
        )
    }

    override fun cropImage(intent: Intent, callback: (ActivityResult) -> Unit) {
        this.callback = callback
        cropImage.launch(intent)
    }
}