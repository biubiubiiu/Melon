package app.melon.im.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.model.Conversation

class ChatViewModel : ViewModel() {

    private val _conversation = MutableLiveData<Conversation>()
    val conversation: LiveData<Conversation> = _conversation

    fun refresh(username: String) {
        val conversation = JMessageClient.getSingleConversation(username)
            ?: Conversation.createSingleConversation(username)
        _conversation.postValue(conversation)
    }
}