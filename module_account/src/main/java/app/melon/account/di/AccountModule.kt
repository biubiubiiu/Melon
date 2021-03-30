package app.melon.account.di

import app.melon.account.FakeAccountService
import app.melon.account.api.IAccountService
import app.melon.account.service.AccountApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(
    includes = [
        AccountServiceBinds::class,
        AccountBuilder::class,
        RemoteServiceModule::class
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

@Module
internal class RemoteServiceModule {
    @Provides
    fun provideAccountService(retrofit: Retrofit) = retrofit.create(AccountApiService::class.java)
}