package app.melon.composer.api

import java.io.Serializable

sealed class ComposerOption : Serializable

data class ContentCreation(
    val accountAvatarUrl: String?
) : ComposerOption()
