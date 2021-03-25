package app.melon.user.ui

import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import app.melon.user.R
import app.melon.user.ui.detail.UserProfileFragment
import app.melon.user.ui.posts.UserPostsFragment
import app.melon.util.event.OnBackPressedHandler
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import dagger.android.support.DaggerFragment

class UserProfileContainerFragment : DaggerFragment(R.layout.fragment_user_profile_container),
    OnBackPressedHandler {

    private val uid by lazy { requireArguments().getString(ARG_USER_ID, "") }

    private val holdTransition = Hold()

    companion object {
        private const val END_FRAGMENT_TRANSITION_NAME = "posts_fragment_transition"

        private val START_FRAGMENT_ROOT_ID = R.id.user_profile_root
        private val END_FRAGMENT_ROOT_ID = R.id.user_posts_root

        private const val ARG_USER_ID = "arg_user_id"
        fun newInstance(uid: String): UserProfileContainerFragment =
            UserProfileContainerFragment().apply {
                arguments = bundleOf(ARG_USER_ID to uid)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProfileFragment()
    }

    private fun showProfileFragment() {
        val fragment = UserProfileFragment.newInstance(uid)
        fragment.configureShowMorePosts {
            showPostsFragment(it)
        }

        holdTransition.addTarget(START_FRAGMENT_ROOT_ID)
        fragment.exitTransition = holdTransition

        childFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
            addToBackStack("UserProfileFragment")
        }
    }

    private fun showPostsFragment(sharedElement: View) {
        val fragment = UserPostsFragment.newInstance(uid,
            END_FRAGMENT_TRANSITION_NAME
        )
        configureTransitions(fragment)

        childFragmentManager.commit {
            addSharedElement(sharedElement,
                END_FRAGMENT_TRANSITION_NAME
            )
            replace(R.id.fragment_container, fragment)
            addToBackStack("UserFeedsFragment")
        }
    }

    private fun configureTransitions(fragment: Fragment) {
        val colorSurface = MaterialColors.getColor(requireView(), R.attr.colorSurface)
        val enterContainerTransform = buildContainerTransform(true)
        enterContainerTransform.setAllContainerColors(colorSurface)
        fragment.sharedElementEnterTransition = enterContainerTransform
        holdTransition.duration = enterContainerTransform.duration
        val returnContainerTransform = buildContainerTransform(false)
        returnContainerTransform.setAllContainerColors(colorSurface)
        fragment.sharedElementReturnTransition = returnContainerTransform
    }

    private fun buildContainerTransform(entering: Boolean): MaterialContainerTransform {
        val transform = MaterialContainerTransform()
        transform.drawingViewId = if (entering) END_FRAGMENT_ROOT_ID else START_FRAGMENT_ROOT_ID
//        configurationHelper.configure(transform, entering)
        // TODO do some configuration
        transform.duration = 300L
        transform.interpolator = LinearInterpolator()
        return transform
    }

    override fun onBackPressed(): Boolean {
        if (view?.findViewById<View>(R.id.user_posts_root) != null) {
            childFragmentManager.popBackStack()
            return true
        }
        return false
    }
}