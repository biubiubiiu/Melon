package app.melon.user.data.mapper

import app.melon.data.entities.MALE
import app.melon.data.entities.User
import app.melon.data.entities.isValidGender
import app.melon.user.data.remote.UserDetailResponse
import app.melon.util.mappers.Mapper
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RemoteUserDetailToUser @Inject constructor() : Mapper<UserDetailResponse, User> {
    override suspend fun map(from: UserDetailResponse): User {
        return User(
            id = from.id,
            username = from.username,
            gender = if (from.gender?.isValidGender() == true) from.gender else MALE,
            age = from.age ?: 0,
            school = from.school,
            location = from.lastLocation ?: "",
            description = from.description ?: "",
            avatarUrl = from.avatarUrl ?: "",
            backgroundUrl = from.backgroundUrl ?: "",
            followingCount = from.followingCount ?: 0L,
            followerCount = from.followerCount ?: 0L
        )
    }
}