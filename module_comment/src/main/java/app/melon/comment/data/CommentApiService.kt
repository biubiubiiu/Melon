package app.melon.comment.data

import app.melon.base.network.MelonApiService
import app.melon.comment.data.remote.CommentStruct
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CommentApiService @Inject constructor(
    private val api: CommentApi
) : MelonApiService() {

    internal suspend fun list(
        id: String,
        page: Int,
        pageSize: Int
    ): Result<List<CommentStruct>> {
        return call {
            api.list(id, page, pageSize)
        }
    }

    suspend fun detail(
        id: String
    ): Result<CommentStruct> {
        return call {
            api.detail(id)
        }
    }

    suspend fun reply(
        id: String,
        page: Int,
        pageSize: Int
    ): Result<List<CommentStruct>> {
        return call {
            api.reply(id, page, pageSize)
        }
    }
}