package app.melon.account.di

import app.melon.account.AccountService
import app.melon.account.UserManagerImpl
import app.melon.account.api.IAccountService
import app.melon.account.api.UserManager
import app.melon.account.data.AccountApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(
    includes = [
        AccountInitModule::class,
        AccountServiceBinds::class,
        AccountBuilder::class,
        UserManagerModuleBinds::class,
        RemoteServiceModule::class
    ]
)
class AccountModule

@Module
internal abstract class AccountServiceBinds {

    @Binds
    internal abstract fun bindsAccountService(service: AccountService): IAccountService
}

@Module
internal abstract class UserManagerModuleBinds {

    @Binds
    internal abstract fun bindsUserManager(userManager: UserManagerImpl): UserManager
}

@Module
internal class RemoteServiceModule {

    @Provides
    internal fun provideAccountService(retrofit: Retrofit) = retrofit.create(AccountApi::class.java)
}