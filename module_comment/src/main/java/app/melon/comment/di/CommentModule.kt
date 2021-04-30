package app.melon.comment.di

import app.melon.base.scope.ActivityScope
import app.melon.base.scope.FragmentScope
import app.melon.comment.CommentReplyActivity
import app.melon.comment.data.CommentApiService
import app.melon.comment.ui.ReplyListFragment
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import retrofit2.Retrofit


@Module(
    includes = [
        RemoteServiceModule::class,
        CommentBuilder::class
    ]
)
class CommentModule

@Module
internal abstract class CommentBuilder {
    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun injectCommentReplyActivity(): CommentReplyActivity

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun injectReplyListFragment(): ReplyListFragment
}

@Module
internal class RemoteServiceModule {
    @Provides
    internal fun provideCommentApiService(retrofit: Retrofit) = retrofit.create(CommentApiService::class.java)
}