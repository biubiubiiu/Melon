package app.melon.composer.api

import android.net.Uri
import app.melon.data.entities.PoiInfo
import java.io.Serializable


data class ComposerResult(
    val textContent: String,
    val images: List<Uri>,
    val location: PoiInfo?
) : Serializable
