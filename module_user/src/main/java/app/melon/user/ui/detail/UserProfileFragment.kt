package app.melon.user.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import app.melon.user.R
import app.melon.user.databinding.FragmentUserProfileBinding
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.viewModelProviderFactoryOf
import app.melon.util.number.MelonNumberFormatter
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class UserProfileFragment : DaggerFragment(R.layout.fragment_user_profile) {

    private val binding: FragmentUserProfileBinding by viewBinding()

    @Inject internal lateinit var viewModelFactory: UserProfileViewModel.Factory
    @Inject internal lateinit var controllerFactory: UserProfileController.Factory

    @Inject internal lateinit var numberFormatter: MelonNumberFormatter

    private val viewModel: UserProfileViewModel by viewModels(::requireActivity) {
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
                    viewModel.viewMorePosts(view)
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userProfileContent.userProfileList.setController(controller)

        viewModel.liveData.observe(viewLifecycleOwner, Observer { state ->
            binding.userProfileRefreshView.root.isVisible = state.refreshing
            controller.setData(state.user, state.pageItems, state.refreshing)
        })
    }

    companion object {
        private const val KEY_USER_ID = "arg_user_id"
        fun newInstance(uid: String): UserProfileFragment =
            UserProfileFragment().apply {
                arguments = bundleOf(KEY_USER_ID to uid)
            }
    }
}