package app.melon.base.framework

import android.content.Context
import app.melon.data.resultentities.EntryWithFeed


abstract class BaseFeedPagingController<T : EntryWithFeed<*>>(
    context: Context
) : BasePagingController<T>(context) {
}