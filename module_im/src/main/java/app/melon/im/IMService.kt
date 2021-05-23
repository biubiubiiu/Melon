package app.melon.im

import android.content.Context
import app.melon.im.message.MessageActivity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class IMService @Inject constructor() : IIMService {
    override fun navigateToMessageActivity(context: Context) {
        MessageActivity.start(context)
    }
}