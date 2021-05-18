package app.melon.composer.api

import java.io.Serializable

sealed class ComposerOption : Serializable

data class ContentCreation(
    val accountAvatarUrl: String?
) : ComposerOption()

object AnonymousPost : ComposerOption()

data class Commentary(
    val authorUid: String,
    val authorUsername: String,
    val accountAvatarUrl: String?
) : ComposerOption()

data class Reply(
    val accountAvatarUrl: String?
) : ComposerOption()