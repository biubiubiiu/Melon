package app.melon.util.di

import android.content.Context
import app.melon.util.storage.ApplicationStorage
import app.melon.util.storage.LoginStorage
import app.melon.util.storage.RegistrationStorage
import app.melon.util.storage.SharedPreferencesStorage
import app.melon.util.storage.Storage
import dagger.Module
import dagger.Provides


@Module
class StorageModule {

    @RegistrationStorage
    @Provides
    fun provideRegistrationStorage(context: Context): Storage {
        return SharedPreferencesStorage("registration", context)
    }

    @LoginStorage
    @Provides
    fun provideLoginStorage(context: Context): Storage {
        return SharedPreferencesStorage("login", context)
    }

    @ApplicationStorage
    @Provides
    fun provideAppStorage(context: Context): Storage {
        return SharedPreferencesStorage("app", context)
    }
}