package app.melon.comment

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CommentService @Inject constructor() : ICommentService {

    override fun postComment(context: Context, feedId: String, content: String) {
        PostCommentService.postComment(context, feedId, content)
    }

    override fun postReply(context: Context, commentId: String, content: String) {
        PostCommentService.postReply(context, commentId, content)
    }
}