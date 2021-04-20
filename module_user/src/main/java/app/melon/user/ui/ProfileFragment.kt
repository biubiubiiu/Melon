package app.melon.user.ui

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import app.melon.user.EditProfileActivity
import app.melon.user.FollowersActivity
import app.melon.user.FollowingActivity
import app.melon.user.ProfileImageActivity
import app.melon.user.R
import app.melon.user.databinding.ContentProfileHeaderBinding
import app.melon.user.databinding.FragmentProfileBinding
import app.melon.user.ui.detail.UserProfileFragment
import app.melon.user.ui.detail.UserProfileViewModel
import app.melon.user.ui.detail.UserProfileViewState
import app.melon.user.ui.detail.create
import app.melon.user.ui.mine.MyProfileTabFragment
import app.melon.util.delegates.viewBinding
import app.melon.util.event.OnBackPressedHandler
import app.melon.util.extensions.getColorCompat
import app.melon.util.extensions.resolveTheme
import app.melon.util.extensions.viewModelProviderFactoryOf
import app.melon.util.number.MelonNumberFormatter
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.appbar.AppBarLayout
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.math.min


class ProfileFragment : DaggerFragment(R.layout.fragment_profile), OnBackPressedHandler {

    private val binding: FragmentProfileBinding by viewBinding()
    private val headerBinding: ContentProfileHeaderBinding get() = binding.header

    private val uid: String get() = arguments?.getString(KEY_USER_ID) ?: ""
    private val isMyProfile get() = uid == "fake_uid" // TODO use UserManager instead

    private val fragmentContainerId get() = binding.container.id
    private val currentFragment: Fragment?
        get() = childFragmentManager.findFragmentById(fragmentContainerId)

    @Inject internal lateinit var viewModelFactory: UserProfileViewModel.Factory
    @Inject internal lateinit var formatter: MelonNumberFormatter

    private val viewModel: UserProfileViewModel by viewModels(::requireActivity) {
        viewModelProviderFactoryOf {
            viewModelFactory.create(
                id = uid
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            val fragment = if (isMyProfile) {
                MyProfileTabFragment()
            } else {
                UserProfileFragment.newInstance(uid)
            }
            childFragmentManager.commit {
                add(fragmentContainerId, fragment)
            }
        }
        initData()
        setupToolbar()
        setupAnimation()
        setupListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (currentFragment?.onOptionsItemSelected(item) == true) {
            return true
        }
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed(): Boolean {
        return (currentFragment as? OnBackPressedHandler)?.onBackPressed() ?: false
    }

    private fun setupToolbar() {
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    private fun setupAnimation() {
        val backgroundHeight = resources.getDimensionPixelSize(R.dimen.my_profile_background_height)
        val actionBarHeight =
            TypedValue.complexToDimensionPixelSize(requireContext().resolveTheme(android.R.attr.actionBarSize),
                resources.displayMetrics)
        val triggerEdge = backgroundHeight - actionBarHeight

        val avatarSize = resources.getDimensionPixelSize(R.dimen.my_profile_avatar_size)
        headerBinding.avatar.pivotX = 0f
        headerBinding.avatar.pivotY = avatarSize.toFloat()
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val ratio = min((verticalOffset * -1f) / triggerEdge, 1f)
            headerBinding.avatar.scaleX = 1 - 0.5f * ratio
            headerBinding.avatar.scaleY = 1 - 0.5f * ratio
        })

        binding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                    binding.toolbar.setTitleTextColor(Color.TRANSPARENT)
                    binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
                }
                if (triggerEdge + verticalOffset <= 0) {
                    binding.toolbar.setBackgroundColor(requireContext().getColorCompat(R.color.bgSecondary))
                    binding.toolbar.setTitleTextColor(appBarLayout.context.getColorCompat(R.color.TextPrimary))
                    isShow = true
                } else if (isShow) {
                    binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
                    binding.toolbar.setTitleTextColor(Color.TRANSPARENT)
                    isShow = false
                }
            }
        })
    }

    private fun setupListener() {
        headerBinding.editProfile.setOnClickListener {
            EditProfileActivity.start(requireContext())
        }
    }

    private fun initData() {
        viewModel.selectObserve(UserProfileViewState::user).observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            with(headerBinding) {
                background.load(it.backgroundUrl)
                avatar.load(it.avatarUrl) {
                    transformations(CircleCropTransformation())
                }
                avatar.setOnClickListener { _ ->
                    ProfileImageActivity.start(requireContext(), it.avatarUrl, isMyProfile)
                }
                followingEntry.setOnClickListener { _ ->
                    FollowingActivity.start(requireContext(), it.id)
                }
                followersEntry.setOnClickListener { _ ->
                    FollowersActivity.start(requireContext(), it.id)
                }
                profileUsername.text = it.username
                profileUserId.text = "todo user id" // TODO
                profileSchoolInfo.text = it.school
                tvFollowers.text = formatter.format(it.followerCount ?: 0)
                tvFollowing.text = formatter.format(it.followingCount ?: 0)
            }
            with(binding) {
                toolbar.title = it.username
            }
        })
    }

    companion object {
        private const val KEY_USER_ID = "KEY_USER_ID"

        fun newInstance(uid: String): ProfileFragment {
            return ProfileFragment().apply {
                arguments = bundleOf(KEY_USER_ID to uid)
            }
        }
    }
}