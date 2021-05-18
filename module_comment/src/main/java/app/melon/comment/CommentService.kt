package app.melon.comment

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CommentService @Inject constructor() : ICommentService {

    override fun postComment(context: Context, feedId: String, content: String) {
        PostCommentService.postComment(context, feedId, content)
    }
}