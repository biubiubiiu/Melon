package app.melon.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import app.melon.data.entities.Feed
import app.melon.feed.databinding.FragmentFeedActionsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


internal class FeedActionsFragment : BottomSheetDialogFragment() {

    private val feed get() = requireArguments().getSerializable(KEY_FEED) as Feed

    internal var onCollectListener: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedActionsBinding.inflate(inflater, container, false)
        binding.collect.text = if (feed.isCollected) {
            getString(R.string.feed_remove_from_collection)
        } else {
            getString(R.string.feed_add_to_collection)
        }
        binding.collect.setOnClickListener {
            onCollectListener.invoke()
            dismiss()
        }
        return binding.root
    }

    companion object {
        private const val KEY_FEED = "key_feed"

        internal fun newInstance(feed: Feed) = FeedActionsFragment().apply {
            arguments = bundleOf(KEY_FEED to feed)
        }
    }
}