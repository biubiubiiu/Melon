package app.melon.bookmark

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import app.melon.R
import app.melon.base.ui.databinding.ActivityCommonFragmentWithToolbarBinding
import app.melon.data.constants.BOOKMARK_FEEDS
import app.melon.feed.FeedPageConfig
import app.melon.feed.ui.CommonFeedFragment
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.getColorCompat


class BookmarkActivity : AppCompatActivity() {

    private val binding: ActivityCommonFragmentWithToolbarBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        setupListFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        binding.toolbar.setTitle(R.string.bookmark_label)
        binding.toolbar.setNavigationIconTint(getColorCompat(R.color.colorPrimary))
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setupListFragment() {
        supportFragmentManager.takeIf { it.findFragmentByTag(FRAGMENT_TAG) == null }
            ?.commit {
                add(
                    binding.fragmentContainer.id,
                    CommonFeedFragment.newInstance(
                        pageName = "",
                        config = FeedPageConfig(
                            pageType = BOOKMARK_FEEDS,
                            idPrefix = "bookmark",
                            isAnonymousFeed = false
                        )
                    ),
                    FRAGMENT_TAG
                )
            }
    }

    companion object {
        private const val FRAGMENT_TAG = "Bookmark_fragment"

        internal fun start(context: Context) {
            context.startActivity(Intent(context, BookmarkActivity::class.java))
        }
    }
}