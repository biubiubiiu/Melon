package app.melon.im.util

import android.content.Context
import app.melon.im.R
import cn.jpush.im.android.api.content.TextContent
import cn.jpush.im.android.api.enums.ContentType
import cn.jpush.im.android.api.model.Message


val ContentType.shortMessage: Int
    get() = when (this) {
        ContentType.image -> R.string.message_type_image
        ContentType.voice -> R.string.message_type_audio
        ContentType.location -> R.string.message_type_location
        ContentType.file -> R.string.message_type_file
        ContentType.video -> R.string.message_type_video
        ContentType.eventNotification -> R.string.message_type_video
        else -> -1
    }

fun getDisplayMessage(context: Context, message: Message?): String {
    if (message == null) {
        return ""
    }
    val contentType = message.contentType
    val messageRes = contentType.shortMessage
    if (messageRes != -1) {
        return context.getString(messageRes)
    }
    if (contentType == ContentType.text) {
        return (message.content as TextContent).text
    }
    return "Unsupported message type now"
}