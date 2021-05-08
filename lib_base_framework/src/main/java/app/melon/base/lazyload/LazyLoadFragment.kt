package app.melon.base.lazyload

import androidx.viewbinding.ViewBinding
import app.melon.base.framework.BaseFragment
import app.melon.base.framework.lifecycle.FragmentLifecycleObserver


abstract class LazyLoadFragment<V : ViewBinding> : BaseFragment<V>(), IFragmentVisibility {

    // if the fragment is visible to the user.
    private var mIsFragmentVisible = false

    // if the fragment is visible to the user for the first time.
    private var mIsFragmentVisibleFirst = true

    override val isVisibleToUser: Boolean get() = mIsFragmentVisible

    private fun determineFragmentVisible() {
        val parent = parentFragment
        if (parent != null && parent is LazyLoadFragment<*>) {
            if (!parent.isVisibleToUser) {
                // Regard this fragment as invisible when its Parent Fragment is invisible
                return
            }
        }

        @Suppress("DEPRECATION")
        if (isResumed && !isHidden && userVisibleHint && !mIsFragmentVisible) {
            mIsFragmentVisible = true
            onVisible()
            if (mIsFragmentVisibleFirst) {
                mIsFragmentVisibleFirst = false
                onFirstVisible()
            } else {
                onPostFirstVisible()
            }
            determineChildFragmentVisible()
        }
    }

    private fun determineFragmentInvisible() {
        if (mIsFragmentVisible) {
            mIsFragmentVisible = false
            onInvisible()
            determineChildFragmentInvisible()
        }
    }

    private fun determineChildFragmentVisible() {
        childFragmentManager.fragments.forEach {
            if (it is LazyLoadFragment<*>) {
                it.determineFragmentVisible()
            }
        }
    }

    private fun determineChildFragmentInvisible() {
        childFragmentManager.fragments.forEach {
            if (it is LazyLoadFragment<*>) {
                it.determineFragmentInvisible()
            }
        }
    }

    inner class LazyLoadObserver : FragmentLifecycleObserver {
        override fun onResume() {
            determineFragmentVisible()
        }

        override fun onPause() {
            determineFragmentInvisible()
        }

        override fun onHiddenChanged(hidden: Boolean) {
            if (hidden) {
                determineFragmentInvisible()
            } else {
                determineFragmentVisible()
            }
        }

        override fun setUserVisibleHint(isVisibleToUser: Boolean) {
            if (isVisibleToUser) {
                determineFragmentVisible()
            } else {
                determineFragmentInvisible()
            }
        }
    }

    init {
        registerObserver(LazyLoadObserver())
    }
}