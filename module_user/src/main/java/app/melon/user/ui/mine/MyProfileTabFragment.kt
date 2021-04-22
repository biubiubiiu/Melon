package app.melon.user.ui.mine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.melon.base.event.TabReselectEvent
import app.melon.base.lazyload.LazyFragmentPagerAdapter
import app.melon.base.ui.databinding.FragmentCommonTabsBinding
import app.melon.data.constants.MY_ANONYMOUS_POST
import app.melon.data.constants.MY_FAVORITE_POST
import app.melon.data.constants.MY_POST
import app.melon.feed.FeedPageConfig
import app.melon.feed.ui.CommonFeedFragment
import app.melon.user.R
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.getResourceString
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment


class MyProfileTabFragment : DaggerFragment(R.layout.fragment_common_tabs) {

    private val binding: FragmentCommonTabsBinding by viewBinding()

    private val viewPager get() = binding.viewpager
    private val tabLayout get() = binding.tabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.adapter = object : LazyFragmentPagerAdapter(childFragmentManager) {

            private val pageConfig = arrayOf(
                FeedPageConfig(MY_ANONYMOUS_POST, "my_anonymous_posts", true),
                FeedPageConfig(MY_POST, "my_posts", false),
                FeedPageConfig(MY_FAVORITE_POST, "my_favorite_posts", false)
            )
            private val pages = titles.size

            override fun getItem(position: Int): Fragment {
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
        private val titles = arrayOf(
            getResourceString(R.string.my_profile_anonymous_posts),
            getResourceString(R.string.my_profile_posts),
            getResourceString(R.string.my_profile_favors)
        )
    }
}