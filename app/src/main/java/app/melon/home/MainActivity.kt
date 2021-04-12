package app.melon.home

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import app.melon.MainViewModel
import app.melon.R
import app.melon.account.api.IAccountService
import app.melon.event.api.IEventService
import app.melon.user.api.IUserService
import app.melon.util.extensions.reverse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(R.layout.activity_main) {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    @Inject internal lateinit var viewModel: MainViewModel

    @Inject internal lateinit var accountService: IAccountService
    @Inject internal lateinit var userService: IUserService
    @Inject internal lateinit var eventService: IEventService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        findViews()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.home_bottom_navigation)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val headerView = navView.getHeaderView(0)
        val navViewHeader: View = headerView.findViewById(R.id.home_nav_header_root)
        val expandMoreIcon: ImageView = headerView.findViewById(R.id.home_nav_expand_more_icon)
        navViewHeader.setOnClickListener {
            viewModel.drawerMenuChanged()
        }
        viewModel.drawerMode.observe(this, Observer {
            navView.menu.setGroupVisible(R.id.home_nav_drawer_group_operations, it)
            navView.menu.setGroupVisible(R.id.home_nav_drawer_group_setting, it)
            navView.menu.setGroupVisible(R.id.home_nav_drawer_group_account, it.reverse())

            expandMoreIcon.rotation = if (it) 0f else 180f
        })

        setupDrawerNavigation()
    }

    private fun findViews() {
        drawerLayout = findViewById(R.id.home_drawer_layout)
        navView = findViewById(R.id.home_nav_view)
    }

    private fun setupDrawerNavigation() {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_nav_drawer_nav_to_login -> accountService.startLogin(this)
                R.id.home_nav_drawer_nav_to_registration -> accountService.startRegister(this)
                R.id.home_nav_drawer_nav_to_profile -> userService.navigateToMyProfile(this)
                R.id.home_nav_drawer_nav_to_events -> eventService.navigateToEventList(this)
                else -> {
                } // TODO
            }
            menuItem.isChecked = false
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_tool_bar_menu, menu)
        return true
    }
}