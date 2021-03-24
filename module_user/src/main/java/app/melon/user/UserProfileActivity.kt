package app.melon.user

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import app.melon.base.uikit.TagView
import app.melon.base.utils.getColorCompat
import coil.load
import coil.transform.CircleCropTransformation
import com.airbnb.epoxy.EpoxyRecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.sankuai.waimai.router.annotation.RouterUri
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


@RouterUri(path = ["/user_profile"])
class UserProfileActivity : DaggerAppCompatActivity(R.layout.activity_user_profile) {

    private val uid: String by lazy(LazyThreadSafetyMode.NONE) { intent.getStringExtra("USER_PROFILE_UID") ?: "" }

    @Inject lateinit var viewModel: UserProfileViewModel

    private val controller by lazy(LazyThreadSafetyMode.NONE) {
        UserProfileController(this)
    }

    private lateinit var toolbar: Toolbar
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var appBarLayout: AppBarLayout

    private lateinit var recyclerView: EpoxyRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchData()
        findViews()

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                    toolbar.setTitleTextColor(Color.TRANSPARENT)
                    toolbar.background.alpha = 0
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar.background.alpha = 1
                    toolbar.setTitleTextColor(appBarLayout.context.getColorCompat(R.color.TextPrimary))
                    isShow = true
                } else if (isShow) {
                    toolbar.background.alpha = 0
                    toolbar.setTitleTextColor(Color.TRANSPARENT)
                    isShow = false
                }
            }
        })

        recyclerView.setController(controller)

        viewModel.liveData.observe(this, Observer { state ->
            findViewById<View>(R.id.user_profile_refresh_view).isVisible = state.refreshing

            val user = state.user ?: return@Observer
            controller.user = user
            toolbar.title = user.username
            findViewById<ImageView>(R.id.user_profile_background).load(user.backgroundUrl)
            findViewById<ImageView>(R.id.user_profile_avatar).load(user.avatarUrl) {
                placeholder(app.melon.base.ui.R.drawable.image_placeholder)
                transformations(CircleCropTransformation())
            }
            findViewById<TextView>(R.id.user_profile_username).text = user.username
            findViewById<TextView>(R.id.user_profile_description).text = user.description
            findViewById<TextView>(R.id.uesr_profile_followers).text = user.followerCount.toString()
            findViewById<TextView>(R.id.user_profile_following).text = user.followingCount.toString()
            findViewById<TagView>(R.id.user_profile_tag).bind(user, style = TagView.TagStyle.Info())
            findViewById<TagView>(R.id.user_profile_distance_tag).bind(
                user,
                style = TagView.TagStyle.Distance(distance = 2f)
            ) // TODO import locate SDK
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.clear()
    }

    private fun findViews() {
        toolbar = findViewById(R.id.toolbar)
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout)
        appBarLayout = findViewById(R.id.app_bar)
        recyclerView = findViewById(R.id.user_profile_list)
    }

    private fun fetchData() {
        viewModel.getUserDetail(uid)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_user_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}