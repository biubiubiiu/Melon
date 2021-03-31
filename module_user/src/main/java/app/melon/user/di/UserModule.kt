package app.melon.user.di

import app.melon.user.UserService
import app.melon.user.api.IUserService
import app.melon.user.data.UserApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(
    includes = [
        UserBuilder::class,
        UserServiceBinds::class,
        RemoteServiceModule::class
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

@Module
internal class RemoteServiceModule {
    @Provides
    fun provideUserApiService(retrofit: Retrofit) = retrofit.create(UserApiService::class.java)
}