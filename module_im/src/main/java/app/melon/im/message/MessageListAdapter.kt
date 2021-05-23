package app.melon.im.message

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.melon.im.R
import app.melon.im.util.DEFAULT_ITEM_DIFF_CALLBACK
import app.melon.im.util.getDisplayMessage
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.model.UserInfo
import coil.load


internal class MessageListAdapter(
    private val onClick: (Conversation) -> Unit
) : ListAdapter<Conversation, MessageViewHolder>(DEFAULT_ITEM_DIFF_CALLBACK as DiffUtil.ItemCallback<Conversation>) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val conversation = getItem(position)
        holder.rootView.tag = conversation
        holder.dot.isVisible = conversation.extra == MessageConstant.MARKER_NEW_MESSAGE
        holder.content.text = getDisplayMessage(holder.rootView.context, conversation.latestMessage)

        val userInfo = conversation.targetInfo as? UserInfo ?: return
        userInfo.getAvatarBitmap(object : GetAvatarBitmapCallback() {
            override fun gotResult(responseCode: Int, responseMessage: String?, avatarBitmap: Bitmap?) {
                if (responseCode == 0) {
                    holder.avatar.load(avatarBitmap)
                } else {
                    holder.avatar.load(R.drawable.default_avatar)
                }
            }
        })
        holder.username.text = userInfo.displayName
    }
}

internal class MessageViewHolder(
    view: View,
    onClick: (Conversation) -> Unit
) : RecyclerView.ViewHolder(view) {

    internal val rootView = view
    internal val avatar: ImageView = view.findViewById(R.id.avatar)
    internal val username: TextView = view.findViewById(R.id.username)
    internal val content: TextView = view.findViewById(R.id.content)
    internal val dot: View = view.findViewById(R.id.new_message_dot)

    init {
        rootView.setOnClickListener {
            val conversation = rootView.tag as? Conversation ?: return@setOnClickListener
            onClick.invoke(conversation)
        }
    }
}