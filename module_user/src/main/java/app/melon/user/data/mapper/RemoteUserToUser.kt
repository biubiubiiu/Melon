package app.melon.user.data.mapper

import app.melon.data.entities.User
import app.melon.data.entities.isValidGender
import app.melon.data.remote.toLocation
import app.melon.user.data.remote.UserListResponse
import app.melon.util.mappers.Mapper
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class RemoteUserToUser @Inject constructor() : Mapper<UserListResponse, User> {
    override suspend fun map(from: UserListResponse): User {
        return User(
            id = from.id,
            username = from.username,
            customId = from.customId,
            gender = if (from.gender?.isValidGender() == true) from.gender else null,
            age = from.age,
            school = from.school,
            description = from.description,
            location = from.lastLocation.toLocation(),
            avatarUrl = from.avatarUrl
        )
    }
}