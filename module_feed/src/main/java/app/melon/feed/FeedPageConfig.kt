package app.melon.feed

import androidx.annotation.StringRes
import app.melon.data.constants.FeedPageType
import java.io.Serializable

data class FeedPageConfig(
    @FeedPageType val pageType: Int,
    val idPrefix: String, // Used as epoxy model id prefix
    val isAnonymousFeed: Boolean, // TODO remove this field
    @StringRes val emptyViewTitleRes: Int? = null,
    @StringRes val emptyViewSubtitleRes: Int? = null
) : Serializable