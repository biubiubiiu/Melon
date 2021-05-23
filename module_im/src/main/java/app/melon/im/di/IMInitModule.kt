package app.melon.im.di

import app.melon.base.initializer.AppInitializer
import app.melon.im.initializer.IMInitializer
import app.melon.im.initializer.JMessageInitializer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet


@Module
internal abstract class IMInitModule {

    @Binds
    @IntoSet
    internal abstract fun bindJMessageInitializer(initializer: JMessageInitializer): AppInitializer

    @Binds
    @IntoSet
    internal abstract fun bindIMInitializer(initializer: IMInitializer): AppInitializer
}