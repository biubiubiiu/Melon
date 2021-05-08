package app.melon.user.di

import app.melon.user.UserService
import app.melon.user.api.IUserService
import app.melon.user.data.UserApi
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
    internal fun provideUserService() = UserService()
}

@Module
internal abstract class UserServiceBinds {
    @Binds
    internal abstract fun bindUserService(service: UserService): IUserService
}

@Module
internal class RemoteServiceModule {
    @Provides
    internal fun provideUserApiService(retrofit: Retrofit) = retrofit.create(UserApi::class.java)
}