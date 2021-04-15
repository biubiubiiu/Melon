package app.melon.user.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import app.melon.base.ui.TagView
import app.melon.user.R
import app.melon.user.databinding.FragmentUserProfileBinding
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.getColorCompat
import app.melon.util.extensions.viewModelProviderFactoryOf
import app.melon.util.number.MelonNumberFormatter
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.appbar.AppBarLayout
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class UserProfileFragment : DaggerFragment(R.layout.fragment_user_profile) {

    private val binding: FragmentUserProfileBinding by viewBinding()

    @Inject internal lateinit var viewModelFactory: UserProfileViewModel.Factory
    @Inject internal lateinit var controllerFactory: UserProfileController.Factory

    @Inject internal lateinit var numberFormatter: MelonNumberFormatter

    private val viewModel: UserProfileViewModel by viewModels {
        viewModelProviderFactoryOf {
            val args = requireArguments()
            viewModelFactory.create(
                id = args.getString(KEY_USER_ID) ?: throw IllegalArgumentException("Missing user id parameter")
            )
        }
    }

    private val controller by lazy(LazyThreadSafetyMode.NONE) {
        controllerFactory.create(
            context = requireContext(),
            action = object : UserProfileController.Action {
                override fun onShowMoreButtonClick(view: View) {
                    showMorePostsCallback?.invoke(view)
                }
            }
        )
    }

    private var showMorePostsCallback: ((View) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        binding.userProfileContent.userProfileList.setController(controller)

        viewModel.liveData.observe(viewLifecycleOwner, Observer { state ->
            binding.userProfileRefreshView.root.isVisible = state.refreshing
            controller.setData(state.user, state.pageItems, state.refreshing)

            val user = state.user ?: return@Observer
            binding.toolbar.title = user.username
            binding.header.userProfileBackground.load(user.backgroundUrl) {
                placeholder(R.drawable.image_placeholder)
            }
            binding.header.userProfileAvatar.load(user.avatarUrl) {
                placeholder(R.drawable.image_placeholder)
                transformations(CircleCropTransformation())
            }
            binding.header.userProfileUsername.text = user.username
            binding.header.userProfileDescription.text = user.description
            binding.header.uesrProfileFollowers.text = numberFormatter.format(user.followerCount ?: 0)
            binding.header.userProfileFollowing.text = numberFormatter.format(user.followerCount ?: 0)
            binding.header.userProfileTag.bind(user, style = TagView.TagStyle.Info())
            binding.header.userProfileDistanceTag.bind(
                user,
                style = TagView.TagStyle.Distance(distance = 2f)
            ) // TODO import locate SDK
        })
    }

    private fun setupToolbar() {
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
        binding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                    binding.toolbar.setTitleTextColor(Color.TRANSPARENT)
                    binding.toolbar.background.alpha = 0
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.toolbar.setTitleTextColor(appBarLayout.context.getColorCompat(R.color.TextPrimary))
                    isShow = true
                } else if (isShow) {
                    binding.toolbar.setTitleTextColor(Color.TRANSPARENT)
                    isShow = false
                }
            }
        })
    }

    fun configureShowMorePosts(
        listener: (View) -> Unit
    ) {
        showMorePostsCallback = listener
    }

    companion object {
        private const val KEY_USER_ID = "arg_user_id"
        fun newInstance(uid: String): UserProfileFragment =
            UserProfileFragment().apply {
                arguments = bundleOf(KEY_USER_ID to uid)
            }
    }
}