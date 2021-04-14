package app.melon.data.constants

import androidx.annotation.IntDef

const val ALL_EVENTS = 0
const val ORGANISED_EVENTS = 1
const val JOINED_EVENTS = 2

@Retention(AnnotationRetention.SOURCE)
@IntDef(ALL_EVENTS, ORGANISED_EVENTS, JOINED_EVENTS)
annotation class EventPageType