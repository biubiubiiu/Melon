package app.melon.util.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract


object GetContentWithMimeType : ActivityResultContract<Pair<String, List<String>>, Uri?>() {

    override fun createIntent(context: Context, input: Pair<String, List<String>>): Intent {
        return Intent(Intent.ACTION_GET_CONTENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType(input.first)
            .putExtra(Intent.EXTRA_MIME_TYPES, input.second.toTypedArray())
    }

    override fun getSynchronousResult(
        context: Context,
        input: Pair<String, List<String>>?
    ): SynchronousResult<Uri?>? {
        return null
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data
    }
}