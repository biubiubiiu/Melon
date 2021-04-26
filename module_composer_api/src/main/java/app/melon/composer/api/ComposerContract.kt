package app.melon.composer.api

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract


class ComposerContract : ActivityResultContract<ComposerOption, ComposerResult?>() {
    override fun createIntent(context: Context, input: ComposerOption?) =
        Intent(ComposerManager.ACTION_START_COMPOSER).apply {
            putExtra(ComposerManager.EXTRA_COMPOSER_OPTION, input)
        }

    override fun parseResult(resultCode: Int, result: Intent?): ComposerResult? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return result?.getSerializableExtra(ComposerManager.EXTRA_COMPOSER_RESULT) as? ComposerResult
    }
}