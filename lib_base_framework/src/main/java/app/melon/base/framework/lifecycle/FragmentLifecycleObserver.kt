package app.melon.base.framework.lifecycle

import android.view.View

interface FragmentLifecycleObserver : LifecycleEventObserver {

    fun onCreate() {}

    fun onViewCreated(view: View) {}

    fun onStart() {}

    fun onResume() {}

    fun onPause() {}

    fun onStop() {}

    fun onDestroy() {}

    fun onHiddenChanged(hidden: Boolean) {}

    fun setUserVisibleHint(isVisibleToUser: Boolean) {}
}