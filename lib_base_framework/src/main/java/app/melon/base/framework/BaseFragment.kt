package app.melon.base.framework

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import app.melon.base.framework.lifecycle.FragmentLifecycleEvent
import app.melon.base.framework.lifecycle.FragmentLifecycleEventDispatcher
import app.melon.base.framework.lifecycle.FragmentLifecycleObserver


abstract class BaseFragment<V : ViewBinding> : FragmentWithBinding<V>() {

    private val mDispatcher = FragmentLifecycleEventDispatcher()

    fun registerObserver(observer: FragmentLifecycleObserver) {
        mDispatcher.registerLifecycleEventObserver(observer)
    }

    fun unregisterObserver(observer: FragmentLifecycleObserver) {
        mDispatcher.unregisterLifecycleEventObserver(observer)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDispatcher.dispatchEvent(FragmentLifecycleEvent.ON_CREATE)
    }

    @CallSuper
    override fun onViewCreated(binding: V, savedInstanceState: Bundle?) {
        mDispatcher.dispatchEvent(FragmentLifecycleEvent.ON_VIEW_CREATED(binding.root))
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        mDispatcher.dispatchEvent(FragmentLifecycleEvent.ON_START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        mDispatcher.dispatchEvent(FragmentLifecycleEvent.ON_RESUME)
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        mDispatcher.dispatchEvent(FragmentLifecycleEvent.ON_STOP)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        mDispatcher.dispatchEvent(FragmentLifecycleEvent.ON_DESTROY)
    }

    @CallSuper
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mDispatcher.dispatchEvent(FragmentLifecycleEvent.ON_HIDDEN_CHANGED(hidden))
    }

    @CallSuper
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        @Suppress("DEPRECATION")
        super.setUserVisibleHint(isVisibleToUser)
        mDispatcher.dispatchEvent(FragmentLifecycleEvent.SET_USER_VISIBLE_HINT(isVisibleToUser))
    }
}