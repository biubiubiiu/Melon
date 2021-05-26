package app.melon.account.data.mapper

import app.melon.account.data.remote.UserDetailResponse
import app.melon.data.entities.User
import app.melon.data.entities.isValidGender
import app.melon.util.mappers.Mapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RemoteUserDetailToUser @Inject constructor() : Mapper<UserDetailResponse, User> {

    override suspend fun map(from: UserDetailResponse): User {
        return User(
            id = from.uid,
            username = from.username,
            customId = from.customId,
            school = from.school,
            age = from.age,
            gender = if (from.gender?.isValidGender() == true) from.gender else null,
            avatarUrl = from.avatarUrl
        )
    }
}