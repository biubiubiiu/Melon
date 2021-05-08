package app.melon.base.framework.lifecycle

abstract class LifecycleEventDispatcher<T : LifecycleEventObserver, E : LifecycleEvent> {

    protected val mObservers: MutableList<T> = ArrayList()

    abstract fun registerLifecycleEventObserver(observer: T)

    abstract fun unregisterLifecycleEventObserver(observer: T)

    abstract fun dispatchEvent(event: E)
}