package app.melon.comment.data.mapper

import app.melon.comment.data.remote.CommentStruct
import app.melon.data.entities.Comment
import app.melon.data.entities.User
import app.melon.data.resultentities.CommentAndAuthor
import app.melon.util.mappers.Mapper
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoteCommentToCommentAndAuthor @Inject constructor() : Mapper<CommentStruct, CommentAndAuthor> {
    override suspend fun map(from: CommentStruct): CommentAndAuthor = process(from)!!

    private fun process(from: CommentStruct?): CommentAndAuthor? {
        if (from == null) {
            return null
        }
        val comment = struct2Comment(from)
        val author = User(
            id = from.author?.id ?: "",
            username = from.author?.username,
            avatarUrl = from.author?.avatarUrl
        )
        val quote = process(from.quote)
        return CommentAndAuthor(comment, author, quote)
    }

    private fun struct2Comment(from: CommentStruct) = Comment(
        id = from.id,
        authorUid = from.author?.id,
        content = from.content,
        replyCount = from.replyCount,
        favorCount = from.favorCount,
        postTime = from.postTime,
        quote = struct2CommentRecursive(from.quote)
    )

    private fun struct2CommentRecursive(from: CommentStruct?): Comment? {
        if (from == null) {
            return null
        }
        return Comment(
            id = from.id,
            authorUid = from.author?.id,
            content = from.content,
            replyCount = from.replyCount,
            favorCount = from.favorCount,
            postTime = from.postTime,
            quote = struct2CommentRecursive(from.quote)
        )
    }
}