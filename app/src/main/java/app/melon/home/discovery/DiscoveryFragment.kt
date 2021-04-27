package app.melon.home.discovery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.melon.R
import app.melon.base.event.TabReselectEvent
import app.melon.base.lazyload.LazyFragmentPagerAdapter
import app.melon.base.ui.databinding.FragmentCommonTabsBinding
import app.melon.data.constants.FOLLOWING_FEED
import app.melon.data.constants.RECOMMEND_FEED
import app.melon.feed.FeedPageConfig
import app.melon.feed.ui.CommonFeedFragment
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.getResourceString
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment


class DiscoveryFragment : DaggerFragment(R.layout.fragment_common_tabs) {

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
                FeedPageConfig(RECOMMEND_FEED, "recommend_feed", false),
                FeedPageConfig(FOLLOWING_FEED, "following_feed", false)
            )
            private val pages = titles.size

            override fun getPageTitle(position: Int): CharSequence = titles[position]

            override fun getItem(position: Int): Fragment {
                if (position in 0 until pages) {
                    return CommonFeedFragment.newInstance(pageName = titles[position],
                        config = pageConfig[position])
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

    private companion object {
        val titles = arrayOf(
            getResourceString(R.string.home_tab_recommend),
            getResourceString(R.string.home_tab_following)
        )
    }
}