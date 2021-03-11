package app.melon.data.di

import app.melon.data.repositories.FeedModule
import app.melon.data.repositories.FollowingFeedModule
import app.melon.data.repositories.RecommendedFeedModule
import dagger.Module


@Module(
    includes = [
        FeedModule::class,
        RecommendedFeedModule::class,
        FollowingFeedModule::class
    ]
)
class RepoModule