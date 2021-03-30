package app.melon.group.di

import app.melon.base.scope.FragmentScope
import app.melon.group.service.GroupApiService
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import retrofit2.Retrofit

@Module(
    includes = [
        RemoteServiceModule::class
    ]
)
abstract class GroupBuilder {
    // to add [app.melon.group.GroupRenderer] to dependency graph
    // couldn't find @AssistedModule yet, so doing this for now
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun injectGroupDummyFragment(): GroupDummyFragment
}

@Module
internal class RemoteServiceModule {
    @Provides
    fun provideGroupService(retrofit: Retrofit) = retrofit.create(GroupApiService::class.java)
}