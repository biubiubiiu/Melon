package app.melon.base.lazyload

import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import dagger.android.support.DaggerFragment


open class LazyLoadFragment(
    @LayoutRes contentLayoutId: Int = 0
) : DaggerFragment(contentLayoutId), IFragmentVisibility {

    // if the fragment is visible to the user.
    private var mIsFragmentVisible = false

    // if the fragment is visible to the user for the first time.
    private var mIsFragmentVisibleFirst = true

    override val isVisibleToUser: Boolean get() = mIsFragmentVisible

    @CallSuper
    override fun onResume() {
        super.onResume()
        determineFragmentVisible()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        determineFragmentInvisible()
    }

    @CallSuper
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (hidden) {
            determineFragmentInvisible()
        } else {
            determineFragmentVisible()
        }
    }

    @CallSuper
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        @Suppress("DEPRECATION")
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
            determineFragmentVisible()
        } else {
            determineFragmentInvisible()
        }
    }

    private fun determineFragmentVisible() {
        val parent = parentFragment
        if (parent != null && parent is LazyLoadFragment) {
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
            if (it is LazyLoadFragment) {
                it.determineFragmentVisible()
            }
        }
    }

    private fun determineChildFragmentInvisible() {
        childFragmentManager.fragments.forEach {
            if (it is LazyLoadFragment) {
                it.determineFragmentInvisible()
            }
        }
    }
}