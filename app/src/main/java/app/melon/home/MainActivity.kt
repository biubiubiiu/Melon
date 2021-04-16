package app.melon.home

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import app.melon.MainViewModel
import app.melon.R
import app.melon.account.api.IAccountService
import app.melon.databinding.ActivityMainBinding
import app.melon.event.api.IEventService
import app.melon.user.api.IUserService
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.reverse
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    @Inject internal lateinit var viewModel: MainViewModel

    @Inject internal lateinit var accountService: IAccountService
    @Inject internal lateinit var userService: IUserService
    @Inject internal lateinit var eventService: IEventService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.mainScreen.toolbar)

//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.mainScreen.homeBottomNavigation.setupWithNavController(navController)

        binding.mainScreen.toolbar.setNavigationOnClickListener {
            binding.homeDrawerLayout.openDrawer(GravityCompat.START)
        }

        setupDrawerHeader()
        setupDrawerNavigation()
    }

    private fun setupDrawerHeader() {
        val headerView = binding.homeNavView.getHeaderView(0)
        val navViewHeader: View = headerView.findViewById(R.id.home_nav_header_root)
        val expandMoreIcon: ImageView = headerView.findViewById(R.id.home_nav_expand_more_icon)
        val followingEntry: View = headerView.findViewById(R.id.following_entry)
        val followerEntry: View = headerView.findViewById(R.id.followers_entry)
        followingEntry.setOnClickListener {
            userService.navigateToFollowingList(this, "fake_uid")
        }
        followerEntry.setOnClickListener {
            userService.navigateToFollowersList(this, "fake_uid")
        }
        navViewHeader.setOnClickListener {
            viewModel.drawerMenuChanged()
        }
        viewModel.drawerMode.observe(this, Observer {
            binding.homeNavView.menu.setGroupVisible(R.id.home_nav_drawer_group_operations, it)
            binding.homeNavView.menu.setGroupVisible(R.id.home_nav_drawer_group_setting, it)
            binding.homeNavView.menu.setGroupVisible(R.id.home_nav_drawer_group_account, it.reverse())

            expandMoreIcon.rotation = if (it) 0f else 180f
        })
    }

    private fun setupDrawerNavigation() {
        binding.homeNavView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_nav_drawer_nav_to_login -> accountService.startLogin(this)
                R.id.home_nav_drawer_nav_to_registration -> accountService.startRegister(this)
                R.id.home_nav_drawer_nav_to_profile -> userService.navigateToMyProfile(this)
                R.id.home_nav_drawer_nav_to_events -> eventService.navigateToEventList(this)
                else -> {
                } // TODO
            }
            menuItem.isChecked = false
            binding.homeDrawerLayout.closeDrawers()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_tool_bar_menu, menu)
        return true
    }
}