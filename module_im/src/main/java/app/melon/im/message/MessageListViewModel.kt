package app.melon.im.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.melon.base.framework.ObservableLoadingCounter
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.enums.ConversationType
import cn.jpush.im.android.api.event.MessageEvent
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.model.UserInfo


class MessageListViewModel : ViewModel() {

    private val _messageList = MutableLiveData<List<Conversation>>()
    val messageList: LiveData<List<Conversation>> = _messageList

    val refreshing = ObservableLoadingCounter()

    fun refresh() {
        refreshing.addLoader()
        val messages = JMessageClient.getConversationList()
        _messageList.postValue(messages)
        refreshing.removeLoader()
    }

    fun markAsRead(conversation: Conversation) {
        val list = _messageList.value?.toMutableList() ?: mutableListOf()
        val index = list.indexOf(conversation)
        if (index != -1) {
            list[index].updateConversationExtra("")
            _messageList.postValue(list)
        }
    }

    fun handleMessageEvent(event: MessageEvent) {
        when (event.message.targetType) {
            ConversationType.single -> handleSingleMessageEvent(event)
            else -> {
                // TODO
            }
        }
    }

    private fun handleSingleMessageEvent(event: MessageEvent) {
        val message = event.message
        val userInfo = message.targetInfo as UserInfo

        var hasHandled = false
        val list = _messageList.value?.toMutableList() ?: mutableListOf()
        list.forEach { bean ->
            if (bean.type == ConversationType.single) {
                val userI = bean.targetInfo as UserInfo
                if (userI.userName.equals(userInfo.userName)) {
                    bean.updateConversationExtra(MessageConstant.MARKER_NEW_MESSAGE)
                    hasHandled = true
                }
            }
        }

        if (!hasHandled) {
            val conversation = JMessageClient.getSingleConversation(userInfo.userName)
            if (conversation.targetInfo is UserInfo) {
                conversation.updateConversationExtra(MessageConstant.MARKER_NEW_MESSAGE)
                list.add(conversation)
            }
        }

        _messageList.postValue(list)
    }
}