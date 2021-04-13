package app.melon.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.melon.base.ui.lazyload.LazyFragmentPagerAdapter
import app.melon.event.databinding.ActivityMyEventsBinding
import app.melon.event.mine.JoiningEventsFragment
import app.melon.event.mine.MyEventsViewModel
import app.melon.event.mine.OrganisedEventsFragment
import app.melon.util.delegates.viewBinding
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MyEventsActivity : DaggerAppCompatActivity() {

    private val binding: ActivityMyEventsBinding by viewBinding()

    private val viewPager get() = binding.viewpager
    private val tabLayout get() = binding.tabLayout

    @Inject internal lateinit var viewModel: MyEventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.back.setOnClickListener { onBackPressed() }

        viewPager.adapter = object : LazyFragmentPagerAdapter(supportFragmentManager) {

            private val titles = arrayOf(
                getString(R.string.my_organisation),
                getString(R.string.my_joining)
            )
            private val pages = titles.size

            override fun getItem(container: ViewGroup?, position: Int): Fragment {
                return when (position) {
                    0 -> JoiningEventsFragment.newInstance(page = 0)
                    1 -> OrganisedEventsFragment.newInstance(page = 1)
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

    companion object {
        internal fun start(context: Context) {
            context.startActivity(Intent(context, MyEventsActivity::class.java))
        }
    }
}