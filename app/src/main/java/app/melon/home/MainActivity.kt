package app.melon.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import app.melon.R
import app.melon.account.api.IAccountService
import app.melon.base.ui.navigation.setupWithNavController
import app.melon.bookmark.BookmarkActivity
import app.melon.databinding.ActivityMainBinding
import app.melon.event.api.IEventService
import app.melon.framework.ComposerEntryActivity
import app.melon.home.nearby.LocateRequest
import app.melon.im.IIMService
import app.melon.location.LocationHelper
import app.melon.permission.PermissionHelper
import app.melon.permission.PermissionHelperOwner
import app.melon.permission.PermissionRequest
import app.melon.settings.SettingsActivity
import app.melon.user.api.IUserService
import app.melon.util.extensions.reverse
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : ComposerEntryActivity(), PermissionHelperOwner, HasAndroidInjector {

    @Inject
    @JvmField
    var androidInjector: DispatchingAndroidInjector<Any>? = null

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var currentNavController: LiveData<NavController>? = null

    private val viewModel: MainViewModel by viewModels()

    @Inject internal lateinit var accountService: IAccountService
    @Inject internal lateinit var userService: IUserService
    @Inject internal lateinit var eventService: IEventService
    @Inject internal lateinit var imService: IIMService

    @Inject internal lateinit var locationHelper: LocationHelper

    private val locatePermissionHelper = PermissionHelper(this, LocateRequest)

    override fun androidInjector(): AndroidInjector<Any> = androidInjector!!

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        startLocate() // start locating ASAP

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer()
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

        setupDrawerHeader()
        setupDrawerNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_tool_bar_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun checkPermission(request: PermissionRequest, onPermissionsGranted: () -> Unit) {
        if (request == LocateRequest) {
            locatePermissionHelper.checkPermissions(
                onPermissionAllGranted = onPermissionsGranted
            )
        }
    }

    private fun startLocate() {
        lifecycleScope.launch {
            locationHelper.tryLocate()
        }
    }

    private fun setupDrawer() {
        viewModel.actionOpenDrawer.observe(this, Observer {
            if (!it.hasBeenHandled) {
                binding.homeDrawerLayout.openDrawer(GravityCompat.START)
            }
        })
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(R.navigation.home, R.navigation.nearby, R.navigation.forum)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = binding.mainScreen.homeBottomNavigation.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = binding.mainScreen.navHostContainer.id,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController, binding.homeDrawerLayout)
        })
        currentNavController = controller
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
                R.id.home_nav_drawer_nav_to_messages -> imService.navigateToMessageActivity(this)
                R.id.home_nav_drawer_nav_to_collections -> BookmarkActivity.start(this)
                R.id.home_nav_drawer_nav_to_settings -> SettingsActivity.start(this)
                else -> {
                } // TODO
            }
            menuItem.isChecked = false
            binding.homeDrawerLayout.closeDrawers()
            true
        }
    }

    companion object {
        internal fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}