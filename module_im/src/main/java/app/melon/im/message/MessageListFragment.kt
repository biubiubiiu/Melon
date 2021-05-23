package app.melon.im.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import app.melon.base.framework.BaseFragment
import app.melon.base.framework.lifecycle.FragmentLifecycleObserver
import app.melon.im.chat.ChatActivity
import app.melon.im.databinding.FragmentMessageListBinding
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.event.MessageEvent
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.model.UserInfo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


internal class MessageListFragment : BaseFragment<FragmentMessageListBinding>() {

    private val viewModel: MessageListViewModel by viewModels()
    private val adapter = MessageListAdapter(::enterChat)

    init {
        registerObserver(JMessageEventObserver())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refresh()

        viewModel.messageList.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMessageListBinding.inflate(inflater, container, false)

    override fun onViewCreated(binding: FragmentMessageListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)
        binding.list.also { v ->
            v.setHasFixedSize(true)
            v.adapter = adapter
        }

        lifecycleScope.launch {
            viewModel.refreshing.observable.collectLatest { refreshing ->
                binding.swipeRefreshLayout.isRefreshing = refreshing
            }
        }
    }

    fun onEventMainThread(event: MessageEvent) {
        viewModel.handleMessageEvent(event)
    }

    private fun enterChat(conversation: Conversation) {
        viewModel.markAsRead(conversation)

        val userInfo = conversation.targetInfo as? UserInfo
        if (userInfo != null) {
            ChatActivity.startSingleChat(requireContext(), userInfo.userName)
        }
    }

    companion object {
        fun newInstance() = MessageListFragment()
    }

    inner class JMessageEventObserver : FragmentLifecycleObserver {
        override fun onCreate() {
            JMessageClient.registerEventReceiver(this@MessageListFragment)
        }

        override fun onDestroy() {
            JMessageClient.unRegisterEventReceiver(this@MessageListFragment)
        }
    }
}