package app.melon.account.di

import app.melon.account.initializer.AccountInitializer
import app.melon.base.initializer.AppInitializer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet


@Module
internal abstract class AccountInitModule {

    @Binds
    @IntoSet
    internal abstract fun bindAccountInitializer(initializer: AccountInitializer): AppInitializer
}