package app.melon.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import app.melon.base.event.TabReselectEvent
import app.melon.base.lazyload.LazyFragmentPagerAdapter
import app.melon.event.databinding.ActivityMyEventsBinding
import app.melon.event.mine.JoiningEventsFragment
import app.melon.event.mine.MyEventsViewModel
import app.melon.event.mine.OrganisedEventsFragment
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.getResourceString
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
            private val pages = titles.size

            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> JoiningEventsFragment.newInstance(pageName = titles[position])
                    1 -> OrganisedEventsFragment.newInstance(pageName = titles[position])
                    else -> throw IllegalArgumentException("out of range")
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

    companion object {
        private val titles = arrayOf(
            getResourceString(R.string.my_organisation),
            getResourceString(R.string.my_joining)
        )

        internal fun start(context: Context) {
            context.startActivity(Intent(context, MyEventsActivity::class.java))
        }
    }
}