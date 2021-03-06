package app.melon.user.data.mapper

import app.melon.data.entities.User
import app.melon.data.entities.isValidGender
import app.melon.data.remote.toLocation
import app.melon.user.data.remote.UserDetailResponse
import app.melon.util.mappers.Mapper
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class RemoteUserDetailToUser @Inject constructor() : Mapper<UserDetailResponse, User> {
    override suspend fun map(from: UserDetailResponse): User {
        return User(
            id = from.id,
            username = from.username,
            customId = from.customId,
            gender = if (from.gender?.isValidGender() == true) from.gender else null,
            age = from.age,
            school = from.school,
            location = from.lastLocation.toLocation(),
            description = from.description,
            avatarUrl = from.avatarUrl,
            photos = from.photos ?: emptyList(),
            backgroundUrl = from.backgroundUrl,
            followingCount = from.followingCount,
            followerCount = from.followerCount
        )
    }
}