package app.melon.data.constants

import androidx.annotation.IntDef

const val RECOMMEND_GROUP = 0
const val JOINED_GROUP = 1

@Retention(AnnotationRetention.SOURCE)
@IntDef(RECOMMEND_GROUP, JOINED_GROUP)
annotation class GroupPageType