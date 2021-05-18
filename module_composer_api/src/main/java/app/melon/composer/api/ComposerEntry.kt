package app.melon.composer.api

interface ComposerEntry {
    fun launchComposer(option: ComposerOption, callback: (ComposerResult?) -> Unit)
}