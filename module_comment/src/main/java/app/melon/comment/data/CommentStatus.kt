package app.melon.comment.data

import app.melon.data.resultentities.CommentAndAuthor

sealed class CommentStatus<out T : Any> {

    class Success(val data: CommentAndAuthor) : CommentStatus<CommentAndAuthor>()

    sealed class Error(val throwable: Throwable) : CommentStatus<Nothing>() {
        class Generic(throwable: Throwable) : Error(throwable)
    }
}