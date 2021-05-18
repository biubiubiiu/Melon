package app.melon.comment.di

import app.melon.base.scope.ActivityScope
import app.melon.base.scope.FragmentScope
import app.melon.comment.CommentReplyActivity
import app.melon.comment.CommentService
import app.melon.comment.ICommentService
import app.melon.comment.PostCommentService
import app.melon.comment.data.CommentApi
import app.melon.comment.ui.ReplyListFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import retrofit2.Retrofit


@Module(
    includes = [
        RemoteServiceModule::class,
        CommentBuilder::class,
        CommentServiceBinds::class
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

    @ContributesAndroidInjector
    internal abstract fun injectPostCommentService(): PostCommentService
}

@Module
internal abstract class CommentServiceBinds {
    @Binds
    internal abstract fun bindsCommentService(service: CommentService): ICommentService
}

@Module
internal class RemoteServiceModule {
    @Provides
    internal fun provideCommentApiService(retrofit: Retrofit) = retrofit.create(CommentApi::class.java)
}