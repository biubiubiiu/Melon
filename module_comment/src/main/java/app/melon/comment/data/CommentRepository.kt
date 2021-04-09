package app.melon.comment.data

import app.melon.data.entities.Comment
import app.melon.data.entities.User
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val service: CommentApiService
) {

    suspend fun fetchCommentList(id: String, page: Int, pageSize: Int): List<Comment> {
        // Testing
        val offset = page * pageSize
        return (0 until pageSize).map { it + offset }.map {
            Comment(
                id = "Comment$it",
                displayPoster = User(
                    id = "User_Test",
                    username = "Test",
                    avatarUrl = "https://img2.baidu.com/it/u=4140499325,1931790298&fm=26&fmt=auto&gp=0.jpg"
                ),
                replyCount = it % 3,
                favorCount = it,
                content = "This is only a test",
                postTime = 123217L,
                displayReply = (0 until it % 3).map { replyId ->
                    Comment(
                        id = "Reply$replyId",
                        displayPoster = User(
                            id = "User_Test",
                            username = "Test"
                        ),
                        content = "TestTest"
                    )
                }
            )
        }
    }
}