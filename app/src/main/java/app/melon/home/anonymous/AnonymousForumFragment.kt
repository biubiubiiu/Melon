package app.melon.home.anonymous

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.melon.base.event.TabReselectEvent
import app.melon.base.ui.databinding.FragmentCommonTabsBinding
import app.melon.base.ui.lazyload.LazyFragmentPagerAdapter
import app.melon.data.constants.ANONYMOUS_ALL_FEED
import app.melon.data.constants.ANONYMOUS_SCHOOL_FEED
import app.melon.data.constants.ANONYMOUS_TRENDING_FEED
import app.melon.feed.FeedPageConfig
import app.melon.feed.R
import app.melon.feed.ui.CommonFeedFragment
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.getResourceString
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment


class AnonymousForumFragment : DaggerFragment(R.layout.fragment_common_tabs) {

    private val binding: FragmentCommonTabsBinding by viewBinding()

    private val viewPager get() = binding.viewpager
    private val tabLayout get() = binding.tabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            setupView()
        }
    }

    private fun setupView() {
        viewPager.adapter = object : LazyFragmentPagerAdapter(childFragmentManager) {
            private val pageConfig = arrayOf(
                FeedPageConfig(ANONYMOUS_SCHOOL_FEED, "anonymous_school_feeds", true),
                FeedPageConfig(ANONYMOUS_ALL_FEED, "anonymous_all_feeds", true),
                FeedPageConfig(ANONYMOUS_TRENDING_FEED, "anonymous_trending_feeds", true)
            )
            private val pages = titles.size

            override fun getItem(container: ViewGroup?, position: Int): Fragment {
                if (position in 0 until pages) {
                    return CommonFeedFragment.newInstance(pageName = titles[position], config = pageConfig[position])
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

    private companion object {
        val titles = arrayOf(
            getResourceString(R.string.feed_school),
            getResourceString(R.string.feed_explore),
            getResourceString(R.string.feed_trending)
        )
    }
}