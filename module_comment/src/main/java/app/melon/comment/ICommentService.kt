package app.melon.comment

import android.content.Context

interface ICommentService {
    fun postComment(context: Context, feedId: String, content: String)
}