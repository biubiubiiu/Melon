package app.melon.user.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.melon.base.event.TabReselectEvent
import app.melon.base.framework.BaseFragment
import app.melon.base.lazyload.LazyFragmentPagerAdapter
import app.melon.base.ui.databinding.FragmentCommonTabsBinding
import app.melon.data.constants.MY_ANONYMOUS_POST
import app.melon.data.constants.MY_FAVORITE_POST
import app.melon.data.constants.MY_POST
import app.melon.feed.FeedPageConfig
import app.melon.feed.ui.CommonFeedFragment
import app.melon.user.R
import app.melon.util.extensions.getResourceString
import com.google.android.material.tabs.TabLayout


internal class MyProfileTabFragment : BaseFragment<FragmentCommonTabsBinding>() {

    private val viewPager get() = binding.viewpager
    private val tabLayout get() = binding.tabLayout

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCommonTabsBinding.inflate(inflater, container, false)

    override fun onViewCreated(binding: FragmentCommonTabsBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)

        viewPager.adapter = object : LazyFragmentPagerAdapter(childFragmentManager) {

            private val pageConfig = arrayOf(
                FeedPageConfig(
                    MY_ANONYMOUS_POST,
                    "my_anonymous_posts",
                    true,
                    R.string.user_no_anonymous_post_title,
                    R.string.user_no_anonymous_post_subtitle
                ),
                FeedPageConfig(
                    MY_POST,
                    "my_posts",
                    false,
                    R.string.user_no_post_title,
                    R.string.user_no_post_subtitle
                ),
                FeedPageConfig(
                    MY_FAVORITE_POST,
                    "my_favorite_posts",
                    false,
                    R.string.user_no_favor_title,
                    R.string.user_no_favor_subtitle
                )
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

    private companion object {
        private val titles = arrayOf(
            getResourceString(R.string.my_profile_anonymous_posts),
            getResourceString(R.string.my_profile_posts),
            getResourceString(R.string.my_profile_favors)
        )
    }
}