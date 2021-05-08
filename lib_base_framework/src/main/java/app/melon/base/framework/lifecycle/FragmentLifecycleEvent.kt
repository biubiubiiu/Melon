package app.melon.base.framework.lifecycle

import android.view.View

sealed class FragmentLifecycleEvent : LifecycleEvent {

    object ON_CREATE : FragmentLifecycleEvent()

    class ON_VIEW_CREATED(val view: View) : FragmentLifecycleEvent()

    object ON_START : FragmentLifecycleEvent()

    object ON_RESUME : FragmentLifecycleEvent()

    object ON_PAUSE : FragmentLifecycleEvent()

    object ON_STOP : FragmentLifecycleEvent()

    object ON_DESTROY : FragmentLifecycleEvent()

    class ON_HIDDEN_CHANGED(val hidden: Boolean) : FragmentLifecycleEvent()

    class SET_USER_VISIBLE_HINT(val isVisibleToUser: Boolean): FragmentLifecycleEvent()
}