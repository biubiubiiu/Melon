package app.melon.user.ui.mine

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.melon.base.ui.databinding.FragmentCommonTabsBinding
import app.melon.base.ui.lazyload.LazyFragmentPagerAdapter
import app.melon.user.R
import app.melon.util.delegates.viewBinding
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MyProfileTabFragment : DaggerFragment(R.layout.fragment_common_tabs) {

    private val binding: FragmentCommonTabsBinding by viewBinding()

    @Inject internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<MyProfileViewModel>(::requireActivity) { viewModelFactory }

    private val viewPager get() = binding.viewpager
    private val tabLayout get() = binding.tabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.adapter = object : LazyFragmentPagerAdapter(childFragmentManager) {

            private val titles = arrayOf(
                getString(R.string.my_profile_anonymous_posts),
                getString(R.string.my_profile_posts),
                getString(R.string.my_profile_favors)
            )
            private val pages = titles.size

            override fun getItem(container: ViewGroup?, position: Int): Fragment {
                return when (position) {
                    0 -> MyPostsFragment.newInstance(page = 0)
                    1 -> MyAnonymousPostsFragment.newInstance(page = 1)
                    2 -> MyFavorPostsFragment.newInstance(page = 2)
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