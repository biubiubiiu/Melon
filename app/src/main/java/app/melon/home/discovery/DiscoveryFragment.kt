package app.melon.home.discovery

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import app.melon.R
import app.melon.account.api.UserManager
import app.melon.base.event.TabReselectEvent
import app.melon.base.lazyload.LazyFragmentPagerAdapter
import app.melon.composer.api.ComposerResult
import app.melon.composer.api.ContentCreation
import app.melon.data.constants.FOLLOWING_FEED
import app.melon.data.constants.RECOMMEND_FEED
import app.melon.databinding.FragmentDiscoveryBinding
import app.melon.feed.FeedPageConfig
import app.melon.feed.PostFeedService
import app.melon.feed.ui.CommonFeedFragment
import app.melon.home.ComposerEntry
import app.melon.home.MainViewModel
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.getResourceString
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class DiscoveryFragment : DaggerFragment(R.layout.fragment_discovery) {

    private val binding: FragmentDiscoveryBinding by viewBinding()

    private val viewPager get() = binding.backbone.viewpager
    private val tabLayout get() = binding.backbone.tabLayout

    private val viewModel: MainViewModel by activityViewModels()

    @Inject internal lateinit var userManager: UserManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            setupView()
        }
    }

    private fun setupView() {
        setupFab()
        setupToolbar()
        setupViewPager()
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            val user = userManager.user ?: return@setOnClickListener
            (activity as? ComposerEntry)?.launchComposer(ContentCreation(user.avatarUrl)) { result ->
                handleComposerResult(result)
            }
        }
    }

    private fun setupToolbar() {
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            viewModel.openDrawer()
        }
    }

    private fun setupViewPager() {
        viewPager.adapter = object : LazyFragmentPagerAdapter(childFragmentManager) {

            private val pageConfig = arrayOf(
                FeedPageConfig(RECOMMEND_FEED, "recommend_feed", false),
                FeedPageConfig(FOLLOWING_FEED, "following_feed", false)
            )
            private val pages = titles.size

            override fun getPageTitle(position: Int): CharSequence = titles[position]

            override fun getItem(position: Int): Fragment {
                if (position in 0 until pages) {
                    return CommonFeedFragment.newInstance(
                        pageName = titles[position],
                        config = pageConfig[position]
                    )
                } else {
                    throw IllegalArgumentException("out of range")
                }
            }

            override fun getCount(): Int = pages
        }
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab ?: return
                TabReselectEvent.sendEvent(tab.view.context, titles[tab.position])
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun handleComposerResult(result: ComposerResult?) {
        val (content, images, location) = result ?: return
        PostFeedService.postFeed(requireContext(), content, ArrayList(images), location)
    }

    private companion object {
        val titles = arrayOf(
            getResourceString(R.string.home_tab_recommend),
            getResourceString(R.string.home_tab_following)
        )
    }
}