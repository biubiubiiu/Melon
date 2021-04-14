package app.melon.data.resultentities

import app.melon.data.entities.Comment
import app.melon.data.entities.User

data class CommentAndAuthor(
    val comment: Comment,
    val author: User,
    val quote: CommentAndAuthor? = null
)