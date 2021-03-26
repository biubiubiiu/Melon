package app.melon.user.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import app.melon.base.uikit.TagView
import app.melon.base.utils.extensions.viewModelProviderFactoryOf
import app.melon.base.utils.getColorCompat
import app.melon.user.R
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyRecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class UserProfileFragment : DaggerFragment(R.layout.fragment_user_profile) {

    @Inject internal lateinit var viewModelFactory: UserProfileViewModel.Factory
    @Inject internal lateinit var controllerFactory: UserProfileController.Factory

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

    private lateinit var toolbar: Toolbar
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var recyclerView: EpoxyRecyclerView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)

        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                    toolbar.setTitleTextColor(Color.TRANSPARENT)
                    toolbar.background.alpha = 0
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar.background.alpha = 1
                    toolbar.setTitleTextColor(appBarLayout.context.getColorCompat(R.color.TextPrimary))
                    isShow = true
                } else if (isShow) {
                    toolbar.background.alpha = 0
                    toolbar.setTitleTextColor(Color.TRANSPARENT)
                    isShow = false
                }
            }
        })
        recyclerView.setController(controller)

        viewModel.liveData.observe(viewLifecycleOwner, Observer { state ->
            view.findViewById<View>(R.id.user_profile_refresh_view).isVisible = state.refreshing
            controller.setData(state.user, state.feeds, state.refreshing)

            val user = state.user ?: return@Observer
            toolbar.title = user.username
            view.findViewById<ImageView>(R.id.user_profile_background).load(user.backgroundUrl) {
                placeholder(app.melon.base.ui.R.drawable.image_placeholder)
            }
            view.findViewById<ImageView>(R.id.user_profile_avatar).load(user.avatarUrl) {
                placeholder(app.melon.base.ui.R.drawable.image_placeholder)
                transformations(CircleCropTransformation())
            }
            view.findViewById<TextView>(R.id.user_profile_username).text = user.username
            view.findViewById<TextView>(R.id.user_profile_description).text = user.description
            view.findViewById<TextView>(R.id.uesr_profile_followers).text = user.followerCount.toString()
            view.findViewById<TextView>(R.id.user_profile_following).text = user.followingCount.toString()
            view.findViewById<TagView>(R.id.user_profile_tag).bind(user, style = TagView.TagStyle.Info())
            view.findViewById<TagView>(R.id.user_profile_distance_tag).bind(
                user,
                style = TagView.TagStyle.Distance(distance = 2f)
            ) // TODO import locate SDK
        })
    }

    private fun findViews(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        collapsingToolbarLayout = view.findViewById(R.id.toolbar_layout)
        appBarLayout = view.findViewById(R.id.app_bar)
        recyclerView = view.findViewById(R.id.user_profile_list)
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