package app.melon.comment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import app.melon.comment.databinding.ActivityCommentReplyBinding
import app.melon.comment.ui.ReplyCommentViewModel
import app.melon.comment.ui.ReplyListFragment
import app.melon.framework.ComposerEntryActivity
import app.melon.util.delegates.viewBinding
import app.melon.util.extensions.getColorCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


internal class CommentReplyActivity : ComposerEntryActivity(), HasAndroidInjector {

    @Inject
    @JvmField
    var androidInjector: DispatchingAndroidInjector<Any>? = null

    private val commentId by lazy { requireNotNull(intent?.getStringExtra(KEY_COMMENT_ID)) }

    private val binding: ActivityCommentReplyBinding by viewBinding()

    @Inject internal lateinit var viewModel: ReplyCommentViewModel
    @Inject internal lateinit var factory: CommentControllerDelegate.Factory

    override fun androidInjector(): AndroidInjector<Any> = androidInjector!!

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        initContent()
        initBottomSheetBehavior()
        initListener()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hideSystemUI()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun hideSystemUI() {
        val color = getColorCompat(R.color.gray_50)
        window.statusBarColor = color
        window.navigationBarColor = color
    }

    private fun initBottomSheetBehavior() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.detailContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED // Expanded by default
        bottomSheetBehavior.halfExpandedRatio = 0.01f
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    finish()
                    // Cancels animation on finish()
                    overridePendingTransition(0, 0)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun initContent() {
        supportFragmentManager.commit {
            replace(binding.fragmentContainer.id, ReplyListFragment.newInstance(commentId))
        }
        viewModel.totalCount.observe(this, Observer {
            binding.totleCommentCountTitle.text = getString(R.string.comment_total_reply_count_title, it)
        })
    }

    private fun initListener() {
        binding.back.setOnClickListener { finish() }
    }

    companion object {
        private const val KEY_COMMENT_ID = "KEY_COMMENT_ID"

        internal fun start(context: Context, id: String) {
            val intent = Intent(context, CommentReplyActivity::class.java).apply {
                putExtra(KEY_COMMENT_ID, id)
            }
            context.startActivity(intent)
        }
    }
}