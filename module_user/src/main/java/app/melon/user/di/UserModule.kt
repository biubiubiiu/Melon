package app.melon.user.di

import app.melon.user.UserService
import app.melon.user.api.IUserService
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        UserBuilder::class,
        UserServiceBinds::class
    ]
)
class UserModule {

    @Singleton
    @Provides
    fun provideUserService() = UserService()
}

@Module
abstract class UserServiceBinds {
    @Binds
    abstract fun bindUserService(service: UserService): IUserService
}