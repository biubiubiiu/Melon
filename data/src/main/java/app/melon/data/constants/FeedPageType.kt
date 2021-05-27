package app.melon.data.constants

import androidx.annotation.IntDef

const val FOLLOWING_FEED = 0
const val RECOMMEND_FEED = 1
const val ANONYMOUS_SCHOOL_FEED = 2
const val ANONYMOUS_TRENDING_FEED = 3
const val ANONYMOUS_ALL_FEED = 4
const val MY_ANONYMOUS_POST = 5
const val MY_POST = 6
const val MY_FAVORITE_POST = 7
const val BOOKMARK_FEEDS = 8
const val POI_FEEDS = 9

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    FOLLOWING_FEED, RECOMMEND_FEED, ANONYMOUS_SCHOOL_FEED, ANONYMOUS_TRENDING_FEED, ANONYMOUS_ALL_FEED, MY_ANONYMOUS_POST,
    MY_POST, MY_FAVORITE_POST, BOOKMARK_FEEDS, POI_FEEDS
)
annotation class FeedPageType