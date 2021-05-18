package app.melon.comment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.melon.comment.data.CommentRepository
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostCommentService : JobIntentService() {

    @Inject internal lateinit var repo: CommentRepository

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private val basicNotificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
        setSmallIcon(R.drawable.ic_guitar)
        priority = NotificationCompat.PRIORITY_DEFAULT
    }

    private var hasCreateNotificationChannel = false

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onHandleWork(intent: Intent) {
        when (intent.action) {
            ACTION_POST_COMMENT -> {
                val feedId = intent.getStringExtra(EXTRA_PARAM_FEED_ID) ?: return
                val content = intent.getStringExtra(EXTRA_PARAM_CONTENT) ?: return
                handleActionPostComment(feedId, content)
            }
            ACTION_POST_REPLY -> {
                val commentId = intent.getStringExtra(EXTRA_PARAM_COMMENT_ID) ?: return
                val content = intent.getStringExtra(EXTRA_PARAM_CONTENT) ?: return
                handleActionPostReply(commentId, content)
            }
        }
    }

    private fun handleActionPostComment(feedId: String, content: String) {
        ioScope.launch {
            sendPostingNotification()
            repo.postComment(feedId, content).onSuccess {
                sendPostSuccessNotification()
            }.onFailure { throwable ->
                sendPostFailNotification(throwable.localizedMessage ?: "")
            }
        }
    }

    private fun handleActionPostReply(commentId: String, content: String) {
        ioScope.launch {
            sendPostingNotification()
            repo.postReply(commentId, content).onSuccess {
                sendPostSuccessNotification()
            }.onFailure { throwable ->
                sendPostFailNotification(throwable.localizedMessage ?: "")
            }
        }
    }

    private fun sendPostingNotification() {
        val builder = basicNotificationBuilder.apply {
            setContentTitle(getString(R.string.post_comment_notification_title))
        }
        ensureNotificationChannel()
        NotificationManagerCompat.from(this).apply {
            builder.setProgress(0, 0, true)
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun sendPostSuccessNotification() {
        val builder = basicNotificationBuilder.apply {
            setContentTitle(getString(R.string.post_comment_success_notification_title))
        }
        ensureNotificationChannel()
        NotificationManagerCompat.from(this).apply {
            builder.setProgress(0, 0, false)
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun sendPostFailNotification(message: String) {
        val builder = basicNotificationBuilder.apply {
            setContentTitle(getString(R.string.post_comment_fail_notification_title))
            setContentText(message)
        }
        ensureNotificationChannel()
        NotificationManagerCompat.from(this).apply {
            builder.setProgress(0, 0, false)
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun ensureNotificationChannel() {
        if (!hasCreateNotificationChannel) {
            createNotificationChannel()
            hasCreateNotificationChannel = true
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            // Register the channel with the system
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val JOB_ID = 1001
        private const val CHANNEL_ID = "post_comment"
        private const val CHANNEL_NAME = "post_comment"
        private const val NOTIFICATION_ID = 101

        private const val ACTION_POST_COMMENT = "app.melon.comment.action.post"
        private const val ACTION_POST_REPLY = "app.melon.comment.action.reply"

        private const val EXTRA_PARAM_FEED_ID = "app.melon.comment.extra.feedId"
        private const val EXTRA_PARAM_COMMENT_ID = "app.melon.comment.extra.commentId"
        private const val EXTRA_PARAM_CONTENT = "app.melon.comment.extra.content"

        fun postComment(
            context: Context,
            feedId: String,
            content: String
        ) {
            val intent = Intent(context, PostCommentService::class.java).apply {
                action = ACTION_POST_COMMENT
                putExtra(EXTRA_PARAM_FEED_ID, feedId)
                putExtra(EXTRA_PARAM_CONTENT, content)
            }
            enqueueWork(context, PostCommentService::class.java, JOB_ID, intent)
        }

        fun postReply(
            context: Context,
            commentId: String,
            content: String
        ) {
            val intent = Intent(context, PostCommentService::class.java).apply {
                action = ACTION_POST_REPLY
                putExtra(EXTRA_PARAM_COMMENT_ID, commentId)
                putExtra(EXTRA_PARAM_CONTENT, content)
            }
            enqueueWork(context, PostCommentService::class.java, JOB_ID, intent)
        }
    }
}