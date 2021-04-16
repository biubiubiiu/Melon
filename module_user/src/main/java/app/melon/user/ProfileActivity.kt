package app.melon.user

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import androidx.lifecycle.Observer
import app.melon.user.databinding.ActivityMyProfileBinding
import app.melon.user.databinding.ContentMyProfileHeaderBinding
import app.melon.user.ui.mine.MyProfileViewModel
import app.melon.util.extensions.getColorCompat
import app.melon.util.extensions.resolveTheme
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.appbar.AppBarLayout
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.math.min


class ProfileActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityMyProfileBinding
    private lateinit var headerBinding: ContentMyProfileHeaderBinding

    @Inject internal lateinit var viewModel: MyProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        headerBinding = binding.header
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initData()
        setupToolbar()
        setupAnimation()
        setupListener()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setupAnimation() {
        val backgroundHeight = resources.getDimensionPixelSize(R.dimen.my_profile_background_height)
        val actionBarHeight =
            TypedValue.complexToDimensionPixelSize(resolveTheme(android.R.attr.actionBarSize), resources.displayMetrics)
        val triggerEdge = backgroundHeight - actionBarHeight

        val avatarSize = resources.getDimensionPixelSize(R.dimen.my_profile_avatar_size)
        headerBinding.avatar.pivotX = 0f
        headerBinding.avatar.pivotY = avatarSize.toFloat()
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val ratio = min((verticalOffset * -1f) / triggerEdge, 1f)
            headerBinding.avatar.scaleX = 1 - 0.5f * ratio
            headerBinding.avatar.scaleY = 1 - 0.5f * ratio
        })

        binding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                    binding.toolbar.setTitleTextColor(Color.TRANSPARENT)
                    binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
                }
                if (triggerEdge + verticalOffset <= 0) {
                    binding.toolbar.setBackgroundColor(getColorCompat(R.color.bgSecondary))
                    binding.toolbar.setTitleTextColor(appBarLayout.context.getColorCompat(R.color.TextPrimary))
                    isShow = true
                } else if (isShow) {
                    binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
                    binding.toolbar.setTitleTextColor(Color.TRANSPARENT)
                    isShow = false
                }
            }
        })
    }

    private fun setupListener() {
        headerBinding.avatar.setOnClickListener {
            ProfileImageActivity.start(this, viewModel.user.value!!.avatarUrl, true)
        }
        headerBinding.editProfile.setOnClickListener {
            EditProfileActivity.start(this)
        }
        headerBinding.followingEntry.setOnClickListener {
            FollowingActivity.start(this, viewModel.user.value!!.id)
        }
        headerBinding.followersEntry.setOnClickListener {
            FollowersActivity.start(this, viewModel.user.value!!.id)
        }
    }

    private fun initData() {
        viewModel.user.observe(this, Observer {
            with(headerBinding) {
                background.load(it.backgroundUrl)
                avatar.load(it.avatarUrl) {
                    transformations(CircleCropTransformation())
                }
                profileUsername.text = it.username
                profileUserId.text = "TODO"
                profileSchoolInfo.text = it.school
                tvFollowers.text = it.followerCount.toString()
                tvFollowing.text = it.followingCount.toString()
            }
            with(binding) {
                toolbar.title = it.username
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ProfileActivity::class.java))
        }
    }
}