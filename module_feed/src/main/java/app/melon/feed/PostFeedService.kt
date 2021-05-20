package app.melon.feed

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.melon.data.entities.PoiInfo
import app.melon.feed.data.FeedRepository
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * An [JobIntentService] subclass for handling feed posting task
 */
class PostFeedService : JobIntentService() {

    @Inject internal lateinit var repo: FeedRepository

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
            ACTION_POST_FEED -> {
                val content = intent.getStringExtra(EXTRA_PARAM_CONTENT) ?: ""
                val images = intent.getParcelableArrayListExtra<Uri>(EXTRA_PARAM_IMAGES) ?: arrayListOf()
                val location = intent.getSerializableExtra(EXTRA_PARAM_LOCATION) as? PoiInfo
                handleActionPostFeed(content, images, location)
            }
        }
    }

    private fun handleActionPostFeed(content: String, images: ArrayList<Uri>, location: PoiInfo?) {
        ioScope.launch {
            sendPostingNotification()
            repo.postFeed(content, images, location).onSuccess {
                sendPostSuccessNotification()
            }.onFailure { throwable ->
                sendPostFailNotification(throwable.localizedMessage ?: "")
            }
        }
    }

    private fun sendPostingNotification() {
        val builder = basicNotificationBuilder.apply {
            setContentTitle(getString(R.string.post_feed_notification_title))
        }
        ensureNotificationChannel()
        NotificationManagerCompat.from(this).apply {
            builder.setProgress(0, 0, true)
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun sendPostSuccessNotification() {
        val builder = basicNotificationBuilder.apply {
            setContentTitle(getString(R.string.post_feed_success_notification_title))
        }
        ensureNotificationChannel()
        NotificationManagerCompat.from(this).apply {
            builder.setProgress(0, 0, false)
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun sendPostFailNotification(message: String) {
        val builder = basicNotificationBuilder.apply {
            setContentTitle(getString(R.string.post_feed_fail_notification_title))
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
        private const val JOB_ID = 1000
        private const val CHANNEL_ID = "post_feed"
        private const val CHANNEL_NAME = "post_feed"
        private const val NOTIFICATION_ID = 100

        private const val ACTION_POST_FEED = "app.melon.feed.action.post"

        private const val EXTRA_PARAM_CONTENT = "app.melon.feed.extra.content"
        private const val EXTRA_PARAM_IMAGES = "app.melon.feed.extra.images"
        private const val EXTRA_PARAM_LOCATION = "app.melon.feed.extra.location"

        fun postFeed(
            context: Context,
            content: String = "",
            images: ArrayList<String> = arrayListOf(),
            location: PoiInfo? = null
        ) {
            postFeedWithUri(
                context,
                content,
                ArrayList(images.map { Uri.parse(it) }),
                location
            )
        }

        fun postFeedWithUri(
            context: Context,
            content: String = "",
            images: ArrayList<Uri> = arrayListOf(),
            location: PoiInfo? = null
        ) {
            val intent = Intent(context, PostFeedService::class.java).apply {
                action = ACTION_POST_FEED
                putExtra(EXTRA_PARAM_CONTENT, content)
                putParcelableArrayListExtra(EXTRA_PARAM_IMAGES, images)
                putExtra(EXTRA_PARAM_LOCATION, location)
            }
            enqueueWork(context, PostFeedService::class.java, JOB_ID, intent)
        }
    }
}