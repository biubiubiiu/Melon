package app.melon.feed.anonymous

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.melon.base.ui.databinding.FragmentCommonTabsBinding
import app.melon.base.ui.lazyload.LazyFragmentPagerAdapter
import app.melon.feed.R
import app.melon.feed.anonymous.ui.ExploreFeedFragment
import app.melon.feed.anonymous.ui.SchoolFeedFragment
import app.melon.feed.anonymous.ui.TrendingFeedFragment
import app.melon.util.delegates.viewBinding
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class AnonymousFeedFragment : DaggerFragment(R.layout.fragment_common_tabs) {

    private val binding: FragmentCommonTabsBinding by viewBinding()

    @Inject internal lateinit var viewModel: AnonymousFeedViewModel

    private val viewPager get() = binding.viewpager
    private val tabLayout get() = binding.tabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.adapter = object : LazyFragmentPagerAdapter(childFragmentManager) {

            private val titles = arrayOf(
                requireContext().getString(R.string.feed_school),
                requireContext().getString(R.string.feed_explore),
                requireContext().getString(R.string.feed_trending)
            )
            private val pages = titles.size

            override fun getItem(container: ViewGroup?, position: Int): Fragment {
                return when (position) {
                    0 -> SchoolFeedFragment.newInstance(page = 0)
                    1 -> ExploreFeedFragment.newInstance(page = 1)
                    2 -> TrendingFeedFragment.newInstance(page = 2)
                    else -> throw IllegalArgumentException("out of range")
                }
            }

            override fun getPageTitle(position: Int): CharSequence? = titles[position]

            override fun getCount(): Int = pages
        }
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab ?: return
                viewModel.reselect(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
            }
        })
    }
}