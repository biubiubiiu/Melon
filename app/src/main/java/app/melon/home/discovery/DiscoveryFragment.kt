package app.melon.home.discovery

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.melon.R
import app.melon.base.framework.BaseMvRxEpoxyFragment
import app.melon.base.lazyload.LazyFragmentPagerAdapter
import app.melon.databinding.FragmentDiscoveryBinding
import app.melon.home.following.ui.FollowFragment
import app.melon.home.recommend.ui.RecommendFragment
import app.melon.utils.viewBinding
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment
import java.lang.ref.WeakReference

class DiscoveryFragment : DaggerFragment(R.layout.fragment_discovery), MavericksView {

    private val binding: FragmentDiscoveryBinding by viewBinding()
    private val viewModel: DiscoveryViewModel by fragmentViewModel()

    private val viewPager get() = binding.homeGalleryViewpager
    private val tabLayout get() = binding.homeGalleryTabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.adapter = object : LazyFragmentPagerAdapter(childFragmentManager) {
            private lateinit var fragmentOnScreen: WeakReference<BaseMvRxEpoxyFragment>

            private val titles = arrayOf(
                requireContext().getString(R.string.home_tab_recommend),
                requireContext().getString(R.string.home_tab_following)
            )
            private val pages = titles.size

            override fun getItem(container: ViewGroup?, position: Int): Fragment {
                return when (position) {
                    0 -> RecommendFragment.newInstance(page = 0)
                    1 -> FollowFragment.newInstance(page = 1)
                    else -> throw IllegalArgumentException("out of range")
                }
            }

            override fun getPageTitle(position: Int): CharSequence? = titles[position]

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
                viewModel.reselect(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
            }
        })
    }

    override fun invalidate() {
        // No-op
    }
}