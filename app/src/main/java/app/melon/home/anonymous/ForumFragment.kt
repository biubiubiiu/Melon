package app.melon.home.anonymous

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import app.melon.R
import app.melon.account.api.UserManager
import app.melon.base.event.TabReselectEvent
import app.melon.base.lazyload.LazyFragmentPagerAdapter
import app.melon.composer.api.AnonymousPost
import app.melon.composer.api.ComposerResult
import app.melon.data.constants.ANONYMOUS_ALL_FEED
import app.melon.data.constants.ANONYMOUS_SCHOOL_FEED
import app.melon.data.constants.ANONYMOUS_TRENDING_FEED
import app.melon.databinding.FragmentForumBinding
import app.melon.feed.FeedPageConfig
import app.melon.feed.PostFeedService
import app.melon.feed.ui.CommonFeedFragment
import app.melon.home.ComposerEntry
import app.melon.home.base.HomepageToolbarFragment
import app.melon.util.extensions.getResourceString
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject


class ForumFragment : HomepageToolbarFragment<FragmentForumBinding>() {

    override val toolbar: Toolbar
        get() = binding.toolbar

    private val viewPager get() = binding.backbone.viewpager
    private val tabLayout get() = binding.backbone.tabLayout

    @Inject internal lateinit var userManager: UserManager

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentForumBinding.inflate(inflater, container, false)

    override fun setupView() {
        super.setupView()
        setupFab()
        setupViewPager()
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            userManager.user ?: return@setOnClickListener
            (activity as? ComposerEntry)?.launchComposer(AnonymousPost) { result ->
                handleComposerResult(result)
            }
        }
    }

    private fun setupViewPager() {
        viewPager.adapter = object : LazyFragmentPagerAdapter(childFragmentManager) {
            private val pageConfig = arrayOf(
                FeedPageConfig(ANONYMOUS_SCHOOL_FEED, "anonymous_school_feeds", true),
                FeedPageConfig(ANONYMOUS_ALL_FEED, "anonymous_all_feeds", true),
                FeedPageConfig(ANONYMOUS_TRENDING_FEED, "anonymous_trending_feeds", true)
            )
            private val pages = titles.size

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

            override fun getPageTitle(position: Int): CharSequence = titles[position]

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
            getResourceString(R.string.feed_school),
            getResourceString(R.string.feed_explore),
            getResourceString(R.string.feed_trending)
        )
    }
}