package app.melon.data.constants

import androidx.annotation.IntDef

const val FOLLOWING_FEED = 0
const val RECOMMEND_FEED = 1
const val ANONYMOUS_SCHOOL_FEED = 2
const val ANONYMOUS_TRENDING_FEED = 3
const val ANONYMOUS_ALL_FEED = 4

@Retention(AnnotationRetention.SOURCE)
@IntDef(FOLLOWING_FEED, RECOMMEND_FEED, ANONYMOUS_SCHOOL_FEED, ANONYMOUS_TRENDING_FEED, ANONYMOUS_ALL_FEED)
annotation class FeedPageType