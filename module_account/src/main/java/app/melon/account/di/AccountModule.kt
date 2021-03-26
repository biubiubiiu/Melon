package app.melon.account.di

import app.melon.account.FakeAccountService
import app.melon.account.api.IAccountService
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        AccountServiceBinds::class
    ]
)
class AccountModule {

    @Singleton
    @Provides
    fun provideAccountService() = FakeAccountService()
}

@Module
abstract class AccountServiceBinds {
    @Binds
    abstract fun bindsAccountService(service: FakeAccountService): IAccountService
}