package app.melon.data.constants

import androidx.annotation.IntDef

const val NEARBY_USER = 0
const val FOLLOWING_USER = 1
const val FOLLOWERS_USER = 2

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    NEARBY_USER, FOLLOWING_USER, FOLLOWERS_USER
)
annotation class UserPageType