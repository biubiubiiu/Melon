package app.melon.composer.api

import app.melon.data.entities.PoiInfo
import java.io.Serializable


data class ComposerResult(
    val textContent: String,
    val images: List<String>,
//    val images: List<Uri>, // TODO replace with Parcelable
    val location: PoiInfo?
) : Serializable
