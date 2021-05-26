package app.melon.data.util

import app.melon.data.entities.Event
import app.melon.data.entities.Feed
import app.melon.data.entities.User

fun mergeFeed(local: Feed, remote: Feed) = local.copy(
    id = remote.id,
    authorUid = remote.authorUid ?: local.authorUid,
    groupId = remote.groupId ?: local.groupId,
    title = remote.title ?: local.title,
    content = remote.content ?: local.content,
    photos = remote.photos,
    poiInfo = remote.poiInfo ?: local.poiInfo,
    isFavored = remote.isFavored,
    isCollected = remote.isCollected,
    postTime = remote.postTime ?: local.postTime,
    repostCount = remote.repostCount ?: local.repostCount,
    replyCount = remote.replyCount ?: local.replyCount,
    favouriteCount = remote.favouriteCount ?: local.favouriteCount
)

fun mergeUser(local: User, remote: User) = local.copy(
    id = remote.id,
    username = remote.username ?: local.username,
    customId = remote.customId ?: local.customId,
    gender = remote.gender ?: local.gender,
    age = remote.age ?: local.age,
    hometown = remote.hometown ?: local.hometown,
    school = remote.school ?: local.school,
    college = remote.college ?: local.college,
    major = remote.major ?: local.major,
    degree = remote.degree ?: local.degree,
    location = remote.location ?: local.location,
    photos = remote.photos,
    description = remote.description ?: local.description,
    avatarUrl = remote.avatarUrl ?: local.avatarUrl,
    backgroundUrl = remote.backgroundUrl ?: local.backgroundUrl,
    followerCount = remote.followerCount ?: local.followerCount,
    followingCount = remote.followingCount ?: local.followingCount
)

fun mergeEvent(local: Event, remote: Event) = local.copy(
    id = remote.id,
    organiserUid = remote.organiserUid ?: local.organiserUid,
    title = remote.title ?: local.title,
    content = remote.content ?: local.content,
    type = remote.type ?: local.type,
    startTime = remote.startTime ?: local.startTime,
    endTime = remote.endTime ?: local.endTime,
    location = remote.location ?: local.location,
    cost = remote.cost ?: local.cost,
    demand = remote.demand ?: local.demand,
    status = remote.status ?: local.status
)