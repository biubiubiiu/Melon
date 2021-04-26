package app.melon.composer.api

import java.io.Serializable


data class ComposerResult(
    val textContext: String = ""
) : Serializable
