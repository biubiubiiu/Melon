package app.melon.home.discovery

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.melon.R
import app.melon.base.event.TabReselectEvent
import app.melon.base.framework.BaseMvRxEpoxyFragment
import app.melon.base.ui.databinding.FragmentCommonTabsBinding
import app.melon.base.ui.lazyload.LazyFragmentPagerAdapter
import app.melon.data.constants.FOLLOWING_FEED
import app.melon.data.constants.RECOMMEND_FEED
import app.melon.feed.FeedPageConfig
import app.melon.feed.ui.CommonFeedFragment
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.getResourceString
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment
import java.lang.ref.WeakReference


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
            private lateinit var fragmentOnScreen: WeakReference<BaseMvRxEpoxyFragment>

            private val pageConfig = arrayOf(
                FeedPageConfig(RECOMMEND_FEED, "recommend_feed", false),
                FeedPageConfig(FOLLOWING_FEED, "following_feed", false)
            )
            private val pages = titles.size

            override fun getItem(container: ViewGroup?, position: Int): Fragment {
                if (position in 0 until pages) {
                    return CommonFeedFragment.newInstance(pageName = titles[position],
                        config = pageConfig[position])
                } else {
                    throw IllegalArgumentException("out of range")
                }
            }

            override fun getPageTitle(position: Int): CharSequence = titles[position]

            override fun getCount(): Int = pages

            override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
                super.setPrimaryItem(container, position, `object`)
                fragmentOnScreen = WeakReference(`object` as BaseMvRxEpoxyFragment)
            }
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