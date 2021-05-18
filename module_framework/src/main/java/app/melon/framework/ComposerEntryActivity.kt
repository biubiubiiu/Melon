package app.melon.framework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.melon.composer.api.ComposerEntry
import app.melon.composer.api.ComposerOption
import app.melon.composer.api.ComposerResult


open class ComposerEntryActivity : AppCompatActivity(), ComposerEntry {

    private lateinit var actionLaunchComposer: ComposerEntry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionLaunchComposer = DefaultComposerEntryHandler(this)
    }

    final override fun launchComposer(option: ComposerOption, callback: (ComposerResult?) -> Unit) {
        actionLaunchComposer.launchComposer(option, callback)
    }
}