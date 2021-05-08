package app.melon.base.framework.lifecycle


class FragmentLifecycleEventDispatcher :
    LifecycleEventDispatcher<FragmentLifecycleObserver, FragmentLifecycleEvent>() {

    override fun registerLifecycleEventObserver(observer: FragmentLifecycleObserver) {
        mObservers.add(observer)
    }

    override fun unregisterLifecycleEventObserver(observer: FragmentLifecycleObserver) {
        mObservers.remove(observer)
    }

    override fun dispatchEvent(event: FragmentLifecycleEvent) {
        when (event) {
            FragmentLifecycleEvent.ON_CREATE -> mObservers.forEach { it.onCreate() }
            is FragmentLifecycleEvent.ON_VIEW_CREATED -> mObservers.forEach { it.onViewCreated(event.view) }
            FragmentLifecycleEvent.ON_START -> mObservers.forEach { it.onStart() }
            FragmentLifecycleEvent.ON_RESUME -> mObservers.forEach { it.onResume() }
            FragmentLifecycleEvent.ON_PAUSE -> mObservers.forEach { it.onPause() }
            FragmentLifecycleEvent.ON_STOP -> mObservers.forEach { it.onStop() }
            FragmentLifecycleEvent.ON_DESTROY -> mObservers.forEach { it.onDestroy() }
            is FragmentLifecycleEvent.ON_HIDDEN_CHANGED -> mObservers.forEach { it.onHiddenChanged(event.hidden) }
            is FragmentLifecycleEvent.SET_USER_VISIBLE_HINT -> mObservers.forEach {
                it.setUserVisibleHint(
                    event.isVisibleToUser
                )
            }
        }
    }
}